package de.campusshop.model;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

/**
 * Entity für eine Position in einer Bestellung.
 */
@Audited
@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    /**
     * Zugehörige Bestellung
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Referenziertes Produkt
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Produktname zum Bestellzeitpunkt
     */
    @Column(nullable = false)
    private String productNameSnapshot;

    /**
     * Preis pro Stück zum Bestellzeitpunkt
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPriceSnapshot;

    /**
     * Bestellte Menge
     */
    @Column(nullable = false)
    private int quantity;

    public OrderItem() {
    }

    public OrderItem(Product product,
                     String productNameSnapshot,
                     BigDecimal unitPriceSnapshot,
                     int quantity) {
        this.product = product;
        this.productNameSnapshot = productNameSnapshot;
        this.unitPriceSnapshot = unitPriceSnapshot;
        this.quantity = quantity;
    }

    // --- Getter / Setter ---

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductNameSnapshot() {
        return productNameSnapshot;
    }

    public void setProductNameSnapshot(String productNameSnapshot) {
        this.productNameSnapshot = productNameSnapshot;
    }

    public BigDecimal getUnitPriceSnapshot() {
        return unitPriceSnapshot;
    }

    public void setUnitPriceSnapshot(BigDecimal unitPriceSnapshot) {
        this.unitPriceSnapshot = unitPriceSnapshot;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
