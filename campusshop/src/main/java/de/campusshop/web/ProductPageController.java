package de.campusshop.web;

import de.campusshop.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/products")
public class ProductPageController {

    private final ProductRepository productRepository;

    public ProductPageController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produkt nicht gefunden: " + id));

        model.addAttribute("product", product);
        return "product-details";
    }
}
