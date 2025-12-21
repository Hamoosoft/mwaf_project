package de.campusshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO für:
 * POST /carts/{cartKey}/items
 */
public class AddToCartRequest {

    @NotNull(message = "productId darf nicht null sein")
    private Long productId;

    @Min(value = 1, message = "quantity muss >= 1 sein")
    private int quantity;

    public AddToCartRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
