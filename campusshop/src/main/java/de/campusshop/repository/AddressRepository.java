package de.campusshop.repository;

import de.campusshop.model.Address;
import de.campusshop.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository für Adressen.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(AppUser user);
    /**
     * Liefert alle Adressen eines Users.
     * Standardadresse steht immer zuerst.
     */
    List<Address> findByUserOrderByIsDefaultDescIdAsc(AppUser user);

    /**
     * Findet eine Adresse nur, wenn sie dem User gehört.
     */
    Optional<Address> findByIdAndUser(Long id, AppUser user);
}
