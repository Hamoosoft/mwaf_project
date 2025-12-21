package de.campusshop.service;

import de.campusshop.dto.*;
import de.campusshop.model.Cart;
import de.campusshop.model.CartItem;
import de.campusshop.model.Product;
import de.campusshop.repository.CartRepository;
import de.campusshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CartService enthält die Business-Logik für den Warenkorb.
 *
 * Typische Regeln:
 * - Cart erzeugen
 * - Item hinzufügen: wenn Produkt schon drin-> quantity erhöhen
 * - Menge ändern
 * - Item entfernen
 * - Cart leeren
 */
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    /**
     * Erzeugt einen neuen, leeren Warenkorb und gibt die cartKey zurück.
     */
    public String createCart() {
        String key = UUID.randomUUID().toString();
        Cart cart = new Cart(key);
        cartRepository.save(cart);
        return key;
    }

    /**
     * Lädt einen Cart inkl. Items + Product + Category und gibt ihn als DTO zurück.
     */
    public CartDto getCart(String cartKey) {
        Cart cart = cartRepository.findByCartKeyWithItems(cartKey)
                .orElseThrow(() -> new IllegalArgumentException("Warenkorb mit cartKey " + cartKey + " existiert nicht"));

        return toDto(cart);
    }

    /**
     * Produkt in den Warenkorb legen:
     * - wenn Produkt schon vorhanden-> quantity erhöhen
     * - sonst neues CartItem
     * einfache Bestandsregel:
     * - quantity darf den stock nicht überschreiten
     */
    public CartDto addItem(String cartKey, AddToCartRequest request) {
        Cart cart = cartRepository.findByCartKeyWithItems(cartKey)
                .orElseThrow(() -> new IllegalArgumentException("Warenkorb mit cartKey " + cartKey + " existiert nicht"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Produkt mit ID " + request.getProductId() + " existiert nicht"));

        if (!product.isActive()) {
            throw new IllegalArgumentException("Produkt ist deaktiviert und kann nicht in den Warenkorb gelegt werden");
        }

        // Prüfen, ob Produkt bereits im Warenkorb ist
        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        int desiredQuantity = (existing == null ? 0 : existing.getQuantity()) + request.getQuantity();

        if (desiredQuantity > product.getStock()) {
            throw new IllegalArgumentException("Nicht genug Bestand. Verfügbar: " + product.getStock() + ", gewünscht: " + desiredQuantity);
        }

        if (existing != null) {
            existing.setQuantity(desiredQuantity);
            // unitPrice bleibt wie er war (Preis-Snapshot)
        } else {
            CartItem item = new CartItem(product, request.getQuantity(), product.getPrice());
            cart.addItem(item);
        }

        Cart saved = cartRepository.save(cart);
        return toDto(saved);
    }

    /**
     * Menge eines Items ändern.
     */
    public CartDto updateItemQuantity(String cartKey, Long itemId, UpdateCartItemRequest request) {
        Cart cart = cartRepository.findByCartKeyWithItems(cartKey)
                .orElseThrow(() -> new IllegalArgumentException("Warenkorb mit cartKey " + cartKey + " existiert nicht"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("CartItem mit ID " + itemId + " existiert nicht in diesem Warenkorb"));

        int desiredQuantity = request.getQuantity();
        int stock = item.getProduct().getStock();

        if (desiredQuantity > stock) {
            throw new IllegalArgumentException("Nicht genug Bestand. Verfügbar: " + stock + ", gewünscht: " + desiredQuantity);
        }

        item.setQuantity(desiredQuantity);

        Cart saved = cartRepository.save(cart);
        return toDto(saved);
    }

    public List<CartItemDto> getCartItems(String cartKey) {
         List<CartItemDto> cartItems = new ArrayList<>();

        List<CartItem> items = cartRepository.findByCartKeyWithItems(cartKey).get().getItems();
        for (CartItem item : items) {
            cartItems.add(toItemDto(item));
        }
        return cartItems ;

    }

    /**
     * Item entfernen.
     */
    public CartDto removeItem(String cartKey, Long itemId) {
        Cart cart = cartRepository.findByCartKeyWithItems(cartKey)
                .orElseThrow(() -> new IllegalArgumentException("Warenkorb mit cartKey " + cartKey + " existiert nicht"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("CartItem mit ID " + itemId + " existiert nicht in diesem Warenkorb"));

        cart.removeItem(item);

        Cart saved = cartRepository.save(cart);
        return toDto(saved);
    }

    /**
     * Warenkorb leeren.
     */
    public void clearCart(String cartKey) {
        Cart cart = cartRepository.findByCartKeyWithItems(cartKey)
                .orElseThrow(() -> new IllegalArgumentException("Warenkorb mit cartKey " + cartKey + " existiert nicht"));

        // orphanRemoval=true sorgt dafür, dass Items in DB gelöscht werden
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // ----------------- Mapping: Entity -> DTO -----------------

    private CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setCartKey(cart.getCartKey());

        List<CartItemDto> itemDtos = cart.getItems().stream()
                .map(this::toItemDto)
                .toList();

        dto.setItems(itemDtos);

        BigDecimal total = itemDtos.stream()
                .map(CartItemDto::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dto.setTotal(total);
        return dto;
    }

    private CartItemDto toItemDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setId(item.getId());

        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductImageUrl(item.getProduct().getImageUrl());

        dto.setCategoryId(item.getProduct().getCategory().getId());
        dto.setCategoryName(item.getProduct().getCategory().getName());
        dto.setCategorySlug(item.getProduct().getCategory().getSlug());

        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());

        BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        dto.setLineTotal(lineTotal);

        return dto;
    }
}
