package de.campusshop.service;

import de.campusshop.dto.CreateProductRequest;
import de.campusshop.dto.ProductDto;
import de.campusshop.dto.UpdateProductRequest;
import de.campusshop.model.Category;
import de.campusshop.model.Product;
import de.campusshop.repository.CategoryRepository;
import de.campusshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service-Schicht:
 * - Business-Logik
 * - DTO Mapping
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductDto> getAllProducts(boolean includeInactive) {
        List<Product> products = includeInactive
                ? productRepository.findAllWithCategory()
                : productRepository.findAllActiveWithCategory();

        return products.stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Gibt Produkte für eine Kategorie (slug) zurück.
     * Standard: nur aktive Produkte.
     * Optional: includeInactive=true => auch inactive Produkte.
     */
    public List<ProductDto> getProductsByCategorySlug(String slug, boolean includeInactive) {
        List<Product> products = includeInactive
                ? productRepository.findAllByCategorySlugWithCategory(slug)
                : productRepository.findActiveByCategorySlugWithCategory(slug);

        return products.stream()
                .map(this::toDto)
                .toList();
    }

    public ProductDto createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Kategorie mit ID " + request.getCategoryId() + " existiert nicht"));

        Product product = new Product(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                request.isActive(),
                request.getImageUrl(),
                category
        );

        Product saved = productRepository.save(product);
        return toDto(saved);
    }

    public ProductDto updateProduct(Long productId, UpdateProductRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produkt mit ID " + productId + " existiert nicht"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Kategorie mit ID " + request.getCategoryId() + " existiert nicht"));

        // Optimistic Locking: manueller Versionsvergleich
        if (product.getVersion() == null || !product.getVersion().equals(request.getVersion())) {
            throw new IllegalStateException(
                    "Optimistic Locking Fehler: Produkt wurde zwischenzeitlich geändert. " +
                            "Aktuelle Version=" + product.getVersion() + ", deine Version=" + request.getVersion()
            );
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setActive(request.isActive());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Product saved = productRepository.save(product);
        return toDto(saved);
    }

    public void softDeleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produkt mit ID " + productId + " existiert nicht"));

        product.setActive(false);
        productRepository.save(product);
    }

    private ProductDto toDto(Product p) {
        ProductDto dto = new ProductDto();

        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setStock(p.getStock());
        dto.setActive(p.isActive());
        dto.setImageUrl(p.getImageUrl());

        dto.setCategoryId(p.getCategory().getId());
        dto.setCategoryName(p.getCategory().getName());
        dto.setCategorySlug(p.getCategory().getSlug());

        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());
        dto.setVersion(p.getVersion());

        return dto;
    }
}
