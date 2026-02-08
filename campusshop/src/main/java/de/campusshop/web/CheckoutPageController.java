package de.campusshop.web;

import de.campusshop.dto.CheckoutRequest;
import de.campusshop.model.AppUser;
import de.campusshop.model.PaymentMethod;
import de.campusshop.repository.AddressRepository;
import de.campusshop.repository.AppUserRepository;
import de.campusshop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/checkout")
public class CheckoutPageController {

    private final AppUserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderService orderService;

    public CheckoutPageController(AppUserRepository userRepository,
                                  AddressRepository addressRepository,
                                  OrderService orderService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.orderService = orderService;
    }

    @GetMapping
    public String checkout(Model model, Principal principal) {

        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();

        model.addAttribute("addresses",
                addressRepository.findByUserOrderByIsDefaultDescIdAsc(user));

        // ✅ nur setzen, wenn noch nicht vorhanden (wichtig für Fehler-Return)
        if (!model.containsAttribute("checkout")) {
            CheckoutRequest form = new CheckoutRequest();
            form.setPaymentMethod(PaymentMethod.CARD); // Default
            model.addAttribute("checkout", form);
        }

        model.addAttribute("paymentMethods", PaymentMethod.values());
        return "checkout";
    }


    @PostMapping
    public String placeOrder(@Valid @ModelAttribute("checkout") CheckoutRequest checkout,
                             BindingResult bindingResult,
                             Principal principal,
                             @CookieValue(value = "cartKey", required = false) String cartKey,
                             Model model) {

        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();

        // 1) Form-Validation (addressId / paymentMethod)
        if (bindingResult.hasErrors()) {
            model.addAttribute("addresses",
                    addressRepository.findByUserOrderByIsDefaultDescIdAsc(user));
            model.addAttribute("paymentMethods", PaymentMethod.values());
            model.addAttribute("checkoutError", "Bitte Adresse und Zahlungsmethode auswählen.");
            return "checkout";
        }

        // 2) cartKey prüfen
        if (cartKey == null || cartKey.isBlank()) {
            model.addAttribute("addresses",
                    addressRepository.findByUserOrderByIsDefaultDescIdAsc(user));
            model.addAttribute("paymentMethods", PaymentMethod.values());
            model.addAttribute("checkoutError", "Warenkorb ist leer oder cartKey fehlt.");
            return "checkout";
        }

        // 3) Checkout durchführen + Business-Fehler sauber anzeigen
        try {
            var orderDto = orderService.checkout(cartKey, checkout, user);
            return "redirect:/orders/" + orderDto.getId();
        } catch (IllegalArgumentException ex) {
            model.addAttribute("addresses",
                    addressRepository.findByUserOrderByIsDefaultDescIdAsc(user));
            model.addAttribute("paymentMethods", PaymentMethod.values());
            model.addAttribute("checkoutError", ex.getMessage());
            return "checkout";
        }
    }

}
