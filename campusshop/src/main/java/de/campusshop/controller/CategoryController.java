package de.campusshop.controller;

import de.campusshop.dto.CategoryDto;
import de.campusshop.dto.ProductDto;
import de.campusshop.service.CategoryService;
import de.campusshop.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Kategorien.
 */
@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService,
                              ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * GET /categories/{slug}/products
     * Standard: nur aktive Produkte
     */
    @GetMapping("/categories/{slug}/products")
    public List<ProductDto> getProductsByCategorySlug(
            @PathVariable String slug,
            @RequestParam(name = "includeInactive", defaultValue = "false") boolean includeInactive
    ) {
        return productService.getProductsByCategorySlug(slug, includeInactive);
    }
}
