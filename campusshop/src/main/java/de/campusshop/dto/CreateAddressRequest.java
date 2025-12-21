package de.campusshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO zum Anlegen einer neuen Adresse.
 */
public class CreateAddressRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String street;

    @NotBlank
    @Size(max = 30)
    private String zip;

    @NotBlank
    private String city;

    /**
     * ISO-2 Länder-Code, z.B. "DE"
     */
    @NotBlank
    @Size(min = 2, max = 2)
    private String country;

    private boolean defaultAddress;

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
        return defaultAddress;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}
