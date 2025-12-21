package de.campusshop.dto;

import de.campusshop.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO für eine Bestellung in der API-Ausgabe.
 */
public class OrderDto {

    private Long id;
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime orderedAt;

    /**
     * Optimistic Locking: Version muss in der API-Ausgabe vorhanden sein,
     * damit der Client sie beim Update wieder mitschicken kann.
     */
    private Long version;

    private List<OrderItemDto> items = new ArrayList<>();

    public OrderDto() {
    }

    // Getter & Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }
}
