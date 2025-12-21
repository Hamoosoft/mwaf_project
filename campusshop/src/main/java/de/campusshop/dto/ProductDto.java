package de.campusshop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ProductDto ist das Objekt, das wir über die REST-API als JSON ausgeben.
 * Es ist NICHT unsere Datenbank-Entity.
 * Ziel:
 * - Keine unnötigen Felder
 */
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private boolean active;
    private String imageUrl;

    // Kategorie "flach" im DTO (statt komplettes Category-Objekt)
    private Long categoryId;
    private String categoryName;
    private String categorySlug;

    // Meta-Felder aus BaseEntity (optional, aber oft nützlich)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    public ProductDto() {
    }

    // ----- Getter & Setter -----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
