package de.campusshop.controller;

import de.campusshop.dto.CreateProductRequest;
import de.campusshop.dto.ProductDto;
import de.campusshop.dto.UpdateProductRequest;
import de.campusshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Produkte.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Liefert alle Produkte.
     * Standard: nur aktive Produkte
     * Optional: ?includeInactive=true
     */
    @GetMapping
    public List<ProductDto> getAllProducts(
            @RequestParam(defaultValue = "false") boolean includeInactive
    ) {
        return productService.getAllProducts(includeInactive);
    }

    /**
     * Erstellt ein neues Produkt.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        return productService.createProduct(request);
    }

    /**
     * Aktualisiert ein bestehendes Produkt.
     */
    @PutMapping("/{id}")
    public ProductDto updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        return productService.updateProduct(id, request);
    }

    /**
     * Löscht ein Produkt (Soft Delete).
     * Das Produkt bleibt in der DB, wird aber deaktiviert.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.softDeleteProduct(id);
    }
}
