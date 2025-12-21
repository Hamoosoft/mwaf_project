package de.campusshop.dto;

import de.campusshop.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO für:
 * PUT /orders/{id}/status
 * Wir verlangen:
 * - status: neuer Status
 * - version: Optimistic Locking (damit parallele Updates erkannt werden)
 */
public class UpdateOrderStatusRequest {

    @NotNull(message = "status darf nicht null sein")
    private OrderStatus status;

    @NotNull(message = "version darf nicht null sein (Optimistic Locking)")
    private Long version;

    public UpdateOrderStatusRequest() {
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
