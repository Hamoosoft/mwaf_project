package de.campusshop.controller;

import de.campusshop.dto.AddressDto;
import de.campusshop.dto.CreateAddressRequest;
import de.campusshop.model.AppUser;
import de.campusshop.repository.AppUserRepository;
import de.campusshop.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Adressen (nur für eingeloggte User).
 */
@RestController
@RequestMapping("/api/addresses")
public class AddressController {


    private final AddressService addressService;
    private final AppUserRepository appUserRepository;

    public AddressController(AddressService addressService, AppUserRepository appUserRepository) {
        this.addressService = addressService;
        this.appUserRepository = appUserRepository;
    }

    private AppUser currentUser(UserDetails principal) {
        return appUserRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Eingeloggter User nicht in DB gefunden: " + principal.getUsername()));
    }

    /**
     * POST /addresses
     * neue Adresse anlegen (für eingeloggten User).
     */
    @PostMapping("/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDto create(@Valid @RequestBody CreateAddressRequest request,
                             @AuthenticationPrincipal UserDetails principal) {
        return addressService.createAddress(request, currentUser(principal));
    }

    /**
     * GET /addresses
     * Alle eigenen Adressen anzeigen.
     */
    @GetMapping("/addresses")
    public List<AddressDto> list(@AuthenticationPrincipal UserDetails principal) {
        return addressService.getMyAddresses(currentUser(principal));
    }

    /**
     * DELETE /addresses/{id}
     * Eigene Adresse löschen.
     */
    @DeleteMapping("/addresses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal UserDetails principal) {
        addressService.deleteAddress(id, currentUser(principal));
    }

    /**
     * PUT /addresses/{id}/default
     * Setzt eine Adresse als Default-Adresse.
     */
    @PutMapping("/addresses/{id}/default")
    public AddressDto setDefault(@PathVariable Long id,
                                 @AuthenticationPrincipal UserDetails principal) {
        return addressService.setDefaultAddress(id, currentUser(principal));
    }
}
