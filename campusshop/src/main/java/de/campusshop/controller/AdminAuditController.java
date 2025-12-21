package de.campusshop.controller;

import de.campusshop.dto.ProductAuditEntryDto;
import de.campusshop.service.ProductAuditService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdminAuditController:
 * - enthält Admin-only Audit Endpoints.
 */
@RestController
@RequestMapping("/admin/audit")
public class AdminAuditController {

    private final ProductAuditService productAuditService;

    public AdminAuditController(ProductAuditService productAuditService) {
        this.productAuditService = productAuditService;
    }

    /**
     * GET /admin/audit/products/{id}
     * Gibt die komplette Historie eines Produkts zurück.
     */
    @GetMapping("/products/{id}")
    public List<ProductAuditEntryDto> getProductAudit(@PathVariable Long id) {
        return productAuditService.getProductHistory(id);
    }
}
