package de.campusshop.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Ein Eintrag in der Product-Historie (eine Envers-Revision).
 * rev: Revisionsnummer (steigt bei jeder Änderung)
 * revisionTimestamp: Zeitpunkt der Revision
 * productSnapshot: Zustand des Produkts zu diesem Zeitpunkt
 */
public class ProductAuditEntryDto {

    private Number rev;
    private Instant revisionTimestamp;

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private boolean active;
    private String imageUrl;
    private Long categoryId;

    public ProductAuditEntryDto() {
    }

    // ----- Getter & Setter -----

    public Number getRev() {
        return rev;
    }

    public void setRev(Number rev) {
        this.rev = rev;
    }

    public Instant getRevisionTimestamp() {
        return revisionTimestamp;
    }

    public void setRevisionTimestamp(Instant revisionTimestamp) {
        this.revisionTimestamp = revisionTimestamp;
    }

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
}
