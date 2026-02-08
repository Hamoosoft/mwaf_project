package de.campusshop.web.admin;

import de.campusshop.dto.OrderDto;
import de.campusshop.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrdersController {

    private final OrderService orderService;

    public AdminOrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String list(Model model) {
        List<OrderDto> orders = orderService.getAllOrders(); // ADMIN: alle Orders
        model.addAttribute("orders", orders);
        return "admin/orders"; // -> templates/admin/orders.html
    }
}
