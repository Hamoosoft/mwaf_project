package de.campusshop.web.admin;

import de.campusshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderPageController {

    private final OrderService orderService;

    public AdminOrderPageController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String orders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }
}
