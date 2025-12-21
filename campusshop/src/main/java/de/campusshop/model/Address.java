package de.campusshop.model;

import jakarta.persistence.*;

/**
 * Entity für eine Adresse eines Benutzers.
 */
@Entity
@Table(name = "addresses")
public class Address extends BaseEntity {

    /**
     * Besitzer der Adresse
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String zip;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    /**
     * Standardadresse des Benutzers
     */
    @Column(name = "is_default", nullable = false)
    private boolean isDefault;


    // --- Getter / Setter ---

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isDefaultAddress() {
        return isDefault;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.isDefault = defaultAddress;
    }
}
