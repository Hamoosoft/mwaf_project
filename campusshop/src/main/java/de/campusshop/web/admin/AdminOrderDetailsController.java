package de.campusshop.web.admin;

import de.campusshop.dto.OrderDto;
import de.campusshop.dto.UpdateOrderStatusRequest;
import de.campusshop.model.OrderStatus;
import de.campusshop.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderDetailsController {

    private final OrderService orderService;

    public AdminOrderDetailsController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ✅ Details
    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        OrderDto order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "admin/order-details";
    }

    // ✅ Status ändern (POST)
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam("status") OrderStatus status,
                               @RequestParam("version") Long version,
                               RedirectAttributes ra) {

        try {
            UpdateOrderStatusRequest req = new UpdateOrderStatusRequest();
            req.setStatus(status);
            req.setVersion(version);

            orderService.updateOrderStatus(id, req);
            ra.addFlashAttribute("success", "✅ Status wurde gespeichert.");

        } catch (Exception e) {
            ra.addFlashAttribute("error", "❌ " + e.getMessage());
        }

        return "redirect:/admin/orders/" + id;
    }
}
