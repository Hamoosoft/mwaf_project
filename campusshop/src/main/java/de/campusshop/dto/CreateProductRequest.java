package de.campusshop.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO zum Erstellen eines Produkts.
 * Wird bei POST /products benutzt.
 */
public class CreateProductRequest {

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
     * Standard: Produkt ist aktiv
     */
    private boolean active = true;

    private String imageUrl;

    @NotNull(message = "categoryId darf nicht null sein")
    private Long categoryId;

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
}
