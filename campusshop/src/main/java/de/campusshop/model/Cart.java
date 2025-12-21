package de.campusshop.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity für einen Warenkorb.
 */
@Entity
@Table(
        name = "carts",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_carts_cart_key",
                columnNames = "cart_key"
        )
)
public class Cart extends BaseEntity {

    /**
     * Eindeutiger Schlüssel für den Warenkorb
     * (z.B. aus Cookie oder Session)
     */
    @Column(name = "cart_key", nullable = false, updatable = false)
    private String cartKey;

    /**
     * Positionen im Warenkorb.
     */
    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CartItem> items = new ArrayList<>();

    public Cart() {
    }

    public Cart(String cartKey) {
        this.cartKey = cartKey;
    }

    public String getCartKey() {
        return cartKey;
    }

    public List<CartItem> getItems() {
        return items;
    }

    /**
     * Fügt ein Item zum Warenkorb hinzu
     * und setzt die Beziehung korrekt.
     */
    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }

    /**
     * Entfernt ein Item aus dem Warenkorb
     * und löscht es aus der DB.
     */
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
    }
}
