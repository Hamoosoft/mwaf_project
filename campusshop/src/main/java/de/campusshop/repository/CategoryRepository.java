package de.campusshop.repository;

import de.campusshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository = Datenzugriffsschicht.
 * Spring erzeugt automatisch eine Implementierung für CRUD-Operationen.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
