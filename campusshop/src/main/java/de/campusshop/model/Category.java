package de.campusshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Entity für eine Produktkategorie.
 */
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    /**
     * Anzeigename der Kategorie
     * (z.B. "Schreibwaren")
     */
    private String name;

    /**
     * URL-freundlicher Schlüssel
     * (z.B. "schreibwaren")
     */
    private String slug;

    // JPA braucht einen leeren Konstruktor
    public Category() {
    }

    public Category(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    // --- Getter & Setter ---

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
}
