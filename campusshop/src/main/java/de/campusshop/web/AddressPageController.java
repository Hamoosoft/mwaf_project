package de.campusshop.web;

import de.campusshop.model.Address;
import de.campusshop.model.AppUser;
import de.campusshop.repository.AddressRepository;
import de.campusshop.repository.AppUserRepository;
import de.campusshop.service.AddressService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/addresses")
public class AddressPageController {

    private final AddressRepository addressRepository;
    private final AppUserRepository userRepository;
    private final AddressService addressService;

    public AddressPageController(AddressRepository addressRepository,
                                 AppUserRepository userRepository,
                                 AddressService addressService) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressService = addressService;
    }

    @GetMapping
    public String addresses(Model model, Principal principal) {

        AppUser user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        model.addAttribute("addresses", addressRepository.findByUserOrderByIsDefaultDescIdAsc(user));
        model.addAttribute("address", new Address());

        return "addresses";
    }

    @PostMapping
    public String addAddress(@ModelAttribute Address address, Principal principal) {

        AppUser user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        address.setUser(user);
        address.setDefaultAddress(false);

        addressRepository.save(address);

        return "redirect:/addresses";
    }

    // ✅ FIX: KEIN /addresses/... weil RequestMapping das schon hat
    @PostMapping("/{id}/default")
    public String setDefaultAddress(@PathVariable Long id, Principal principal) {

        AppUser user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        addressService.setDefaultAddress(id, user);

        return "redirect:/addresses";
    }

    @PostMapping("/{id}/delete")
    public String deleteAddress(@PathVariable Long id, Principal principal) {

        AppUser user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        Address address = addressRepository.findById(id).orElseThrow();

        if (address.getUser().getId().equals(user.getId())) {
            addressRepository.delete(address);
        }

        return "redirect:/addresses";
    }
}
