package de.campusshop.repository;

import de.campusshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * ProductRepository = Datenzugriff für Product.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Lädt ALLE Produkte inkl. Kategorie (auch inactive).
     */
    @Query("select p from Product p join fetch p.category")
    List<Product> findAllWithCategory();

    /**
     * Lädt nur aktive Produkte inkl. Kategorie.
     */
    @Query("select p from Product p join fetch p.category where p.active = true")
    List<Product> findAllActiveWithCategory();

    /**
     * Lädt nur aktive Produkte einer Kategorie (über category.slug) inkl. Kategorie.
     * Beispiel: slug = "schreibwaren"
     */
    @Query("select p from Product p join fetch p.category c where p.active = true and c.slug = :slug")
    List<Product> findActiveByCategorySlugWithCategory(String slug);

    /**
     * Lädt ALLE Produkte (active und inactive) einer Kategorie inkl. Kategorie.
     */
    @Query("select p from Product p join fetch p.category c where c.slug = :slug")
    List<Product> findAllByCategorySlugWithCategory(String slug);
}
