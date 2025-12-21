package de.campusshop.dto;

import java.time.LocalDateTime;

/**
 * CategoryDto = JSON-Ausgabe für Kategorien.
 * Wir geben bewusst nur wenige Felder raus (API bleibt übersichtlich).
 */
public class CategoryDto {

    private Long id;
    private String name;
    private String slug;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    public CategoryDto() {
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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
