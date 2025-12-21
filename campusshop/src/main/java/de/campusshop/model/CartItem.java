package de.campusshop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entity für eine Position im Warenkorb.
 */
@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {

    /**
     * Zugehöriger Warenkorb
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /**
     * Produkt dieser Position
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Anzahl des Produkts
     */
    @Column(nullable = false)
    private int quantity;

    /**
     * Preis pro Stück zum Zeitpunkt des Hinzufügens.
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    public CartItem() {
    }

    public CartItem(Product product, int quantity, BigDecimal unitPrice) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // --- Getter / Setter ---

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
