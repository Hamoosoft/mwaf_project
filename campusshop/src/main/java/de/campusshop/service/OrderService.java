package de.campusshop.service;

import de.campusshop.dto.CheckoutRequest;
import de.campusshop.dto.OrderDto;
import de.campusshop.dto.OrderItemDto;
import de.campusshop.dto.UpdateOrderStatusRequest;
import de.campusshop.model.*;
import de.campusshop.repository.AddressRepository;
import de.campusshop.repository.CartRepository;
import de.campusshop.repository.OrderRepository;
import de.campusshop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    public OrderService(CartRepository cartRepository,
                        ProductRepository productRepository,
                        OrderRepository orderRepository,
                        AddressRepository addressRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
    }

    // -------------------- Checkout --------------------

    @Transactional
    public OrderDto checkout(String cartKey, CheckoutRequest request, AppUser currentUser) {

        Cart cart = cartRepository.findByCartKeyWithItems(cartKey)
                .orElseThrow(() -> new IllegalArgumentException("Warenkorb mit cartKey " + cartKey + " existiert nicht"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Warenkorb ist leer. Checkout nicht möglich.");
        }

        // Adresse muss dem User gehören
        Address address = addressRepository.findByIdAndUser(request.getAddressId(), currentUser)
                .orElseThrow(() -> new IllegalArgumentException("Adresse existiert nicht oder gehört nicht zu diesem User"));

        // Produkte prüfen
        for (CartItem item : cart.getItems()) {
            Product p = item.getProduct();

            if (!p.isActive()) {
                throw new IllegalArgumentException("Produkt '" + p.getName() + "' ist deaktiviert. Checkout nicht möglich.");
            }

            if (item.getQuantity() > p.getStock()) {
                throw new IllegalArgumentException(
                        "Nicht genug Bestand für '" + p.getName() + "'. Verfügbar: " + p.getStock() + ", gewünscht: " + item.getQuantity()
                );
            }
        }

        Order order = new Order();
        order.setStatus(OrderStatus.NEW);

        // ✅ NEU: Order gehört dem User
        order.setUser(currentUser);

        // Snapshot Lieferadresse
        order.setShippingFullName(address.getFullName());
        order.setShippingStreet(address.getStreet());
        order.setShippingZip(address.getZip());
        order.setShippingCity(address.getCity());
        order.setShippingCountry(address.getCountry());

        // Payment
        order.setPaymentMethod(request.getPaymentMethod());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product p = cartItem.getProduct();

            BigDecimal price = cartItem.getUnitPrice();
            int qty = cartItem.getQuantity();

            OrderItem orderItem = new OrderItem(
                    p,
                    p.getName(),
                    price,
                    qty
            );

            order.addItem(orderItem);

            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(qty));
            total = total.add(lineTotal);

            // Bestand reduzieren
            p.setStock(p.getStock() - qty);
            productRepository.save(p);
        }

        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);

        // Cart leeren
        cart.getItems().clear();
        cartRepository.save(cart);

        return toDto(savedOrder);
    }

    // -------------------- Orders anzeigen --------------------

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAllWithItems()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new IllegalArgumentException("Order mit ID " + id + " existiert nicht"));

        return toDto(order);
    }
    @Transactional(readOnly = true)
    public OrderDto getMyOrderById(Long orderId, AppUser currentUser) {
        Order order = orderRepository.findByIdAndUserIdWithItems(orderId, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        return toDto(order);
    }


    // ✅ NEU: Orders für aktuellen User
    public List<OrderDto> getMyOrders(AppUser currentUser) {
        return orderRepository.findAllByUserIdWithItems(currentUser.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    // -------------------- Status ändern --------------------

    @Transactional
    public OrderDto updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {

        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order mit ID " + orderId + " existiert nicht"));

        if (order.getVersion() == null || !order.getVersion().equals(request.getVersion())) {
            throw new IllegalStateException(
                    "Optimistic Locking Fehler: Order wurde zwischenzeitlich geändert. " +
                            "Aktuelle Version=" + order.getVersion() + ", deine Version=" + request.getVersion()
            );
        }

        OrderStatus current = order.getStatus();
        OrderStatus target = request.getStatus();

        if (!isTransitionAllowed(current, target)) {
            throw new IllegalStateException(
                    "Ungültiger Statuswechsel: " + current + " -> " + target
            );
        }

        order.setStatus(target);

        Order saved = orderRepository.save(order);

        return toDto(saved);
    }

    private boolean isTransitionAllowed(OrderStatus current, OrderStatus target) {
        if (current == OrderStatus.NEW) {
            return target == OrderStatus.PAID || target == OrderStatus.CANCELLED;
        }
        if (current == OrderStatus.PAID) {
            return target == OrderStatus.SHIPPED || target == OrderStatus.CANCELLED;
        }
        return false;
    }

    // ---------------- Mapping: Entity -> DTO ----------------

    // ---------------- Mapping: Entity -> DTO ----------------

    private OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setVersion(order.getVersion());
        dto.setStatus(order.getStatus());
        dto.setTotal(order.getTotal());
        dto.setOrderedAt(order.getOrderedAt());

        // ✅ NEU: Shipping Snapshot (für order-details.html)
        dto.setShippingFullName(order.getShippingFullName());
        dto.setShippingStreet(order.getShippingStreet());
        dto.setShippingZip(order.getShippingZip());
        dto.setShippingCity(order.getShippingCity());
        dto.setShippingCountry(order.getShippingCountry());

        // ✅ NEU: Payment Method
        dto.setPaymentMethod(order.getPaymentMethod());

        List<OrderItemDto> items = order.getItems().stream()
                .map(this::toItemDto)
                .toList();

        dto.setItems(items);
        return dto;
    }


    private OrderItemDto toItemDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProductNameSnapshot());
        dto.setUnitPrice(item.getUnitPriceSnapshot());
        dto.setQuantity(item.getQuantity());

        BigDecimal lineTotal = item.getUnitPriceSnapshot().multiply(BigDecimal.valueOf(item.getQuantity()));
        dto.setLineTotal(lineTotal);

        return dto;
    }
}
