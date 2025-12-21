package de.campusshop.repository;

import de.campusshop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository für Warenkörbe.
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Findet einen Warenkorb über den Cart-Key.
     */
    Optional<Cart> findByCartKey(String cartKey);

    /**
     * Lädt einen Warenkorb inklusive Items, Produkte und Kategorien.
     * Vermeidet LazyLoading-Probleme.
     */
    @Query("""
           select c from Cart c
           left join fetch c.items i
           left join fetch i.product p
           left join fetch p.category
           where c.cartKey = :cartKey
           """)
    Optional<Cart> findByCartKeyWithItems(String cartKey);
}
