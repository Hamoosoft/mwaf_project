package de.campusshop.dto;

import jakarta.validation.constraints.Min;

/**
 * Request DTO für:
 * PUT /carts/{cartKey}/items/{itemId}
 */
public class UpdateCartItemRequest {

    @Min(value = 1, message = "quantity muss >= 1 sein")
    private int quantity;

    public UpdateCartItemRequest() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
