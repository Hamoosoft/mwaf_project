package de.campusshop.web;

import de.campusshop.service.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CartCountAdvice {

    private final CartService cartService;

    public CartCountAdvice(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Wird AUTOMATISCH bei jeder Seite ausgeführt
     * und legt "cartCount" ins Model
     */
    @ModelAttribute("cartCount")
    public int cartCount(HttpServletRequest request) {

        String cartKey = getCookieValue(request, "cartKey");
        if (cartKey == null || cartKey.isBlank()) {
            return 0;
        }

        try {
            var cart = cartService.getCart(cartKey);

            // ✅ Summe aller quantities im Warenkorb
            return cart.getItems()
                    .stream()
                    .mapToInt(item -> item.getQuantity())
                    .sum();

        } catch (Exception e) {
            // falls Cart nicht existiert / Cookie kaputt
            return 0;
        }
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;

        for (Cookie c : request.getCookies()) {
            if (name.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
}
