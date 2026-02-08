package de.campusshop.web;

import de.campusshop.dto.AddToCartRequest;
import de.campusshop.dto.UpdateCartItemRequest;
import de.campusshop.service.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartPageController {

    private final CartService cartService;

    public CartPageController(CartService cartService) {
        this.cartService = cartService;
    }

    /* ===============================
       Warenkorb anzeigen
       =============================== */
    @GetMapping
    public String viewCart(@CookieValue(value = "cartKey", required = false) String cartKey,
                           HttpServletResponse response,
                           Model model) {

        cartKey = ensureCartKey(cartKey, response);
        model.addAttribute("cart", cartService.getCart(cartKey));
        return "cart";
    }

    /* ===============================
       Produkt hinzufügen
       =============================== */
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            @CookieValue(value = "cartKey", required = false) String cartKey,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            Model model) {

        cartKey = ensureCartKey(cartKey, response);

        try {
            AddToCartRequest req = new AddToCartRequest();
            req.setProductId(productId);
            req.setQuantity(quantity);

            cartService.addItem(cartKey, req);

            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/cart");

        } catch (IllegalArgumentException ex) {
            model.addAttribute("cart", cartService.getCart(cartKey));
            model.addAttribute("cartError", ex.getMessage());
            return "cart";
        }
    }

    /* ===============================
       Menge ändern
       =============================== */
    @PostMapping("/items/{id}")
    public String updateItem(@PathVariable Long id,
                             @RequestParam int quantity,
                             @CookieValue(value = "cartKey", required = false) String cartKey,
                             HttpServletResponse response) {

        cartKey = ensureCartKey(cartKey, response);

        UpdateCartItemRequest req = new UpdateCartItemRequest();
        req.setQuantity(quantity);

        cartService.updateItemQuantity(cartKey, id, req);
        return "redirect:/cart";
    }

    /* ===============================
       Item entfernen
       =============================== */
    @PostMapping("/items/{id}/delete")
    public String deleteItem(@PathVariable Long id,
                             @CookieValue(value = "cartKey", required = false) String cartKey,
                             HttpServletResponse response) {

        cartKey = ensureCartKey(cartKey, response);
        cartService.removeItem(cartKey, id);
        return "redirect:/cart";
    }

    /* ===============================
       Warenkorb leeren
       =============================== */
    @PostMapping("/clear")
    public String clearCart(@CookieValue(value = "cartKey", required = false) String cartKey,
                            HttpServletResponse response) {

        cartKey = ensureCartKey(cartKey, response);
        cartService.clearCart(cartKey);
        return "redirect:/cart";
    }

    /* ===============================
       cartKey sicherstellen
       =============================== */
    private String ensureCartKey(String cartKey, HttpServletResponse response) {
        if (cartKey == null || cartKey.isBlank()) {
            cartKey = cartService.createCart();

            Cookie cookie = new Cookie("cartKey", cartKey);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24 * 30); // 30 Tage
            response.addCookie(cookie);
        }
        return cartKey;
    }
}
