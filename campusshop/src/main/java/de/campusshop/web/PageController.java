package de.campusshop.web;

import de.campusshop.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    private final ProductRepository productRepository;

    public PageController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String index(@RequestParam(name = "q", required = false) String q, Model model) {

        var products = productRepository.findAll(); // später: filter
        if (q != null && !q.isBlank()) {
            String qq = q.toLowerCase();
            products = products.stream()
                    .filter(p -> (p.getName() != null && p.getName().toLowerCase().contains(qq))
                            || (p.getDescription() != null && p.getDescription().toLowerCase().contains(qq)))
                    .toList();
        }

        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("products", products);
        model.addAttribute("countText", products.size() + " Produkte");

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
