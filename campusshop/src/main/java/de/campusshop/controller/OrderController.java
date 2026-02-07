package de.campusshop.controller;

import de.campusshop.dto.CheckoutRequest;
import de.campusshop.dto.OrderDto;
import de.campusshop.dto.UpdateOrderStatusRequest;
import de.campusshop.model.AppUser;
import de.campusshop.repository.AppUserRepository;
import de.campusshop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Bestellungen.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final AppUserRepository appUserRepository;

    public OrderController(OrderService orderService, AppUserRepository appUserRepository) {
        this.orderService = orderService;
        this.appUserRepository = appUserRepository;
    }

    private AppUser currentUser(UserDetails principal) {
        return appUserRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Eingeloggter User nicht in DB gefunden: " + principal.getUsername()));
    }

    @PostMapping("/carts/{cartKey}/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto checkout(@PathVariable String cartKey,
                             @Valid @RequestBody CheckoutRequest request,
                             @AuthenticationPrincipal UserDetails principal) {
        return orderService.checkout(cartKey, request, currentUser(principal));
    }

    /**
     * Admin sieht alle Orders.
     * (Rollen-Regel kommt in SecurityConfig)
     */
    @GetMapping("/orders")
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * Customer sieht nur eigene Orders.
     */
    @GetMapping("/orders/me")
    public List<OrderDto> getMyOrders(@AuthenticationPrincipal UserDetails principal) {
        return orderService.getMyOrders(currentUser(principal));
    }

    @GetMapping("/orders/{id}")
    public OrderDto getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    /**
     * PUT /orders/{id}/status
     * ändert den Status einer Order.
     */
    @PutMapping("/orders/{id}/status")
    public OrderDto updateOrderStatus(@PathVariable Long id,
                                      @Valid @RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateOrderStatus(id, request);
    }
}
