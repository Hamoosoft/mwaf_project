package de.campusshop.dto;

import de.campusshop.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;

/**
 * DTO für den Checkout-Prozess.
 */
public class CheckoutRequest {

    @NotNull
    private Long addressId;

    @NotNull
    private PaymentMethod paymentMethod;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
