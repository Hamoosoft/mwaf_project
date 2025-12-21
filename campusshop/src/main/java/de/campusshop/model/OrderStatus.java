package de.campusshop.model;

/**
 * OrderStatus beschreibt den Lebenszyklus einer Bestellung.
 *
 * Typischer Ablauf:
 * NEW -> PAID -> SHIPPED
 * Oder: NEW -> CANCELLED
 */
public enum OrderStatus {
    NEW,
    PAID,
    SHIPPED,
    CANCELLED
}
