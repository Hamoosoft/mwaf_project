package de.campusshop.repository;

import de.campusshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository für Bestellungen.
 * Nutzt Fetch-Joins, damit Items/Produkte beim Laden direkt verfügbar sind.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Alle Orders inkl. Items und Produkt laden.
     */
    @Query("""
           select distinct o from Order o
           left join fetch o.items i
           left join fetch i.product p
           order by o.orderedAt desc
           """)
    List<Order> findAllWithItems();

    /**
     * Eine Order per ID inkl. Items und Produkt laden.
     */
    @Query("""
           select distinct o from Order o
           left join fetch o.items i
           left join fetch i.product p
           where o.id = :id
           """)
    Optional<Order> findByIdWithItems(@Param("id") Long id);

    /**
     * Alle Orders eines Users inkl. Items laden.
     */
    @Query("""
           select distinct o from Order o
           left join fetch o.items i
           where o.user.id = :userId
           order by o.orderedAt desc
           """)
    List<Order> findAllByUserIdWithItems(@Param("userId") Long userId);

    @Query("""
       select distinct o from Order o
       left join fetch o.items i
       left join fetch i.product p
       where o.id = :id and o.user.id = :userId
       """)
    Optional<Order> findByIdAndUserIdWithItems(@Param("id") Long id, @Param("userId") Long userId);

}
