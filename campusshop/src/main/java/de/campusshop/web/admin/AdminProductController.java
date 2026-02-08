package de.campusshop.web.admin;

import de.campusshop.model.Category;
import de.campusshop.model.Product;
import de.campusshop.repository.CategoryRepository;
import de.campusshop.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public AdminProductController(ProductRepository productRepository,
                                  CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // 1) Liste
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/products";
    }

    // 2) NEW Form
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    // 3) CREATE
    @PostMapping
    public String create(@ModelAttribute("productForm") ProductForm form,
                         Model model) {

        Category cat = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Kategorie nicht gefunden"));

        Product p = new Product();
        p.setName(form.getName());
        p.setDescription(form.getDescription());
        p.setPrice(form.getPrice());
        p.setStock(form.getStock());
        p.setActive(form.isActive());
        p.setImageUrl(form.getImageUrl());
        p.setCategory(cat);

        productRepository.save(p);
        return "redirect:/admin/products";
    }

    // 4) EDIT Form
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produkt nicht gefunden"));

        ProductForm form = ProductForm.fromEntity(p);

        model.addAttribute("productId", id);
        model.addAttribute("productForm", form);
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    // 5) UPDATE
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("productForm") ProductForm form) {

        Product p = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produkt nicht gefunden"));

        Category cat = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Kategorie nicht gefunden"));

        p.setName(form.getName());
        p.setDescription(form.getDescription());
        p.setPrice(form.getPrice());
        p.setStock(form.getStock());
        p.setActive(form.isActive());
        p.setImageUrl(form.getImageUrl());
        p.setCategory(cat);

        productRepository.save(p);
        return "redirect:/admin/products";
    }

    // 6) Toggle active
    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produkt nicht gefunden"));

        p.setActive(!p.isActive());
        productRepository.save(p);

        return "redirect:/admin/products";
    }

    // 7) DELETE (mit Schutz)
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model) {
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            // Falls Orders/CartItems FK haben: dann lieber deaktivieren statt crash
            Product p = productRepository.findById(id).orElse(null);
            if (p != null) {
                p.setActive(false);
                productRepository.save(p);
            }
        }
        return "redirect:/admin/products";
    }

    // --- Simple Form DTO nur für Admin-Formular ---
    public static class ProductForm {
        private String name;
        private String description;
        private BigDecimal price;
        private int stock;
        private boolean active = true;
        private String imageUrl;
        private Long categoryId;

        public static ProductForm fromEntity(Product p) {
            ProductForm f = new ProductForm();
            f.setName(p.getName());
            f.setDescription(p.getDescription());
            f.setPrice(p.getPrice());
            f.setStock(p.getStock());
            f.setActive(p.isActive());
            f.setImageUrl(p.getImageUrl());
            f.setCategoryId(p.getCategory().getId());
            return f;
        }

        // getters/setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }

        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }

        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }

        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    }
}
