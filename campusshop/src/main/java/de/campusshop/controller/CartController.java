package de.campusshop.controller;

import de.campusshop.dto.AddToCartRequest;
import de.campusshop.dto.CartDto;
import de.campusshop.dto.CartItemDto;
import de.campusshop.dto.UpdateCartItemRequest;
import de.campusshop.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Warenkorb.
 */
@RestController
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * POST /carts
     * Erzeugt einen neuen Warenkorb und gibt den cartKey zurück.
     */
    @PostMapping("/carts")
    @ResponseStatus(HttpStatus.CREATED)
    public String createCart() {
        return cartService.createCart();
    }

    /**
     * GET /carts/{cartKey}
     * Gibt Warenkorb + Items zurück.
     */
    @GetMapping("/carts/{cartKey}")
    public CartDto getCart(@PathVariable String cartKey) {
        return cartService.getCart(cartKey);
    }

    @GetMapping("/carts/{cartKey}/all/items")
    public List<CartItemDto> getCartItems(@PathVariable String cartKey) {
        return cartService.getCartItems(cartKey);
    }

    /**
     * POST /carts/{cartKey}/items
     * Produkt in Warenkorb legen (oder Menge erhöhen).
     */
    @PostMapping("/carts/{cartKey}/items")
    public CartDto addItem(@PathVariable String cartKey,
                           @Valid @RequestBody AddToCartRequest request) {
        return cartService.addItem(cartKey, request);
    }

    /**
     * PUT /carts/{cartKey}/items/{itemId}
     * Menge ändern.
     */
    @PutMapping("/carts/{cartKey}/items/{itemId}")
    public CartDto updateItemQuantity(@PathVariable String cartKey,
                                      @PathVariable Long itemId,
                                      @Valid @RequestBody UpdateCartItemRequest request) {
        return cartService.updateItemQuantity(cartKey, itemId, request);
    }

    /**
     * DELETE /carts/{cartKey}/items/{itemId}
     * Item entfernen.
     */
    @DeleteMapping("/carts/{cartKey}/items/{itemId}")
    public CartDto removeItem(@PathVariable String cartKey,
                              @PathVariable Long itemId) {
        return cartService.removeItem(cartKey, itemId);
    }

    /**
     * DELETE /carts/{cartKey}
     * Warenkorb leeren.
     */
    @DeleteMapping("/carts/{cartKey}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable String cartKey) {
        cartService.clearCart(cartKey);
    }
}
