package de.campusshop.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * BaseEntity ist eine "Mapped Superclass".
 * - Diese Klasse wird NICHT als eigene Tabelle gespeichert.
 * - Aber alle Klassen, die davon erben (extends BaseEntity).
 * - So vermeiden wir Code-Duplikate (id, createdAt, updatedAt, version)
 */
@MappedSuperclass
public abstract class BaseEntity {


    // Primärschlüssel in der Datenbank wird Auto-erzeugt.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Zeitpunkt, wann der Datensatz erstellt wurde.
     * Wird automatisch gesetzt, wenn ein Objekt zum ersten Mal gespeichert wird.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Zeitpunkt, wann der Datensatz zuletzt geändert wurde.
     * Wird automatisch bei jedem Update gesetzt.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Optimistic Locking:
     * - Bei jeder Änderung wird diese Version hochgezählt.
     * - Wenn zwei Personen gleichzeitig denselben Datensatz ändern,
     *   verhindert JPA, dass Änderungen "aus Versehen" überschrieben werden.
     */
    @Version
    private Long version;

    /**
     * Diese Methode wird automatisch aufgerufen, BEVOR das Objekt
     * zum ersten Mal in der DB gespeichert wird.
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Diese Methode wird automatisch aufgerufen, BEVOR ein bestehender Datensatz
     * aktualisiert (updated) wird.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ----- Getter (und für Felder, die man setzen darf ggf. Setter) -----

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }
}
