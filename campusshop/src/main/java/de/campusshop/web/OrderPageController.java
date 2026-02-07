package de.campusshop.web;

import de.campusshop.model.AppUser;
import de.campusshop.repository.AppUserRepository;
import de.campusshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderPageController {

    private final AppUserRepository userRepository;
    private final OrderService orderService;

    public OrderPageController(AppUserRepository userRepository, OrderService orderService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    // ✅ Bestellübersicht
    @GetMapping
    public String orders(Principal principal, Model model) {
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("orders", orderService.getMyOrders(user));
        return "orders";
    }

    // ✅ Details
    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Principal principal, Model model) {
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("order", orderService.getMyOrderById(id, user));
        return "order-details";
    }
}
