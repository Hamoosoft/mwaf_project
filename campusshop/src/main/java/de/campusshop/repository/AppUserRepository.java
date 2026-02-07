package de.campusshop.repository;

import de.campusshop.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository für Benutzer.
 */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    /**
     * Findet einen Benutzer über den Benutzernamen.
     * Wird für den Login benutzt.
     */
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
