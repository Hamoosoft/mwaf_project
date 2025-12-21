package de.campusshop.service;

import de.campusshop.dto.ProductAuditEntryDto;
import de.campusshop.model.Product;
import jakarta.persistence.EntityManager;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * ProductAuditService liest die Produkt-Historie aus Envers aus.
 *
 * Technisch:
 * - AuditReader ist die Envers-API zum Lesen der Audit-Tabellen.
 * - getRevisions(Product.class, id) liefert alle Revision-Nummern für dieses Produkt.
 * - find(Product.class, id, rev) liefert den Zustand (Snapshot) des Produkts in dieser Revision.
 */
@Service
public class ProductAuditService {

    private final EntityManager entityManager;

    public ProductAuditService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<ProductAuditEntryDto> getProductHistory(Long productId) {
        AuditReader reader = AuditReaderFactory.get(entityManager);

        List<Number> revisions = reader.getRevisions(Product.class, productId);

        if (revisions == null || revisions.isEmpty()) {
            throw new IllegalArgumentException("Keine Audit-Historie gefunden für Product ID " + productId +
                    " (Existiert das Produkt? Wurde es jemals geändert?)");
        }

        // Für jede Revision Snapshot holen und in DTO umwandeln
        List<ProductAuditEntryDto> history = revisions.stream()
                .map(rev -> {
                    Product p = reader.find(Product.class, productId, rev);
                    if (p == null) {
                        return null;
                    }

                    ProductAuditEntryDto dto = new ProductAuditEntryDto();
                    dto.setRev(rev);

                    dto.setRevisionTimestamp(Instant.ofEpochMilli(reader.getRevisionDate(rev).getTime()));

                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setDescription(p.getDescription());
                    dto.setPrice(p.getPrice());
                    dto.setStock(p.getStock());
                    dto.setActive(p.isActive());
                    dto.setImageUrl(p.getImageUrl());

                    dto.setCategoryId(p.getCategory() != null ? p.getCategory().getId() : null);

                    return dto;
                })
                .filter(x -> x != null)
                // Neueste Revision zuerst:
                // Wir vergleichen hier rev.longValue(), weil Number nicht Comparable ist.
                .sorted(Comparator.comparingLong(
                        (ProductAuditEntryDto e) -> e.getRev().longValue()
                ).reversed())

                .toList();

        return history;
    }
}
