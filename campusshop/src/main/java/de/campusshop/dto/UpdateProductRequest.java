package de.campusshop.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO zum Aktualisieren eines Produkts.
 * Wird bei PUT /products/{id} benutzt.
 */
public class UpdateProductRequest {

    @NotBlank(message = "name darf nicht leer sein")
    private String name;

    @NotBlank(message = "description darf nicht leer sein")
    private String description;

    @NotNull(message = "price darf nicht null sein")
    @DecimalMin(value = "0.00", message = "price muss >= 0.00 sein")
    private BigDecimal price;

    @Min(value = 0, message = "stock muss >= 0 sein")
    private int stock;

    /**
     * Produkt aktiv oder inaktiv
     */
    private boolean active = true;

    private String imageUrl;

    @NotNull(message = "categoryId darf nicht null sein")
    private Long categoryId;

    /**
     * Version für Optimistic Locking.
     * Kommt aus einem vorherigen GET /products.
     */
    @NotNull(message = "version darf nicht null sein")
    private Long version;

    // ----- Getter & Setter -----

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
