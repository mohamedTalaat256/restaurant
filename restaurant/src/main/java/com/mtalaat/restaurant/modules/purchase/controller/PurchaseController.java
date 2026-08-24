package com.mtalaat.restaurant.modules.purchase.controller;

import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.purchase.dto.PurchaseDto;
import com.mtalaat.restaurant.modules.purchase.service.PurchaseService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.purchase-id}")
    private Long menuId;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody PurchaseDto dto) {
        permissionChecker.checkCreate(menuId);
        PurchaseDto created = purchaseService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_purchase_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        PurchaseDto purchase = purchaseService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_purchase_fetched", purchase, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_purchase_fetched", purchaseService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody PurchaseDto dto) {
        permissionChecker.checkEdit(menuId);
        PurchaseDto updated = purchaseService.updatePurchase(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_purchase_updated", updated, status.value()));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse> approve(@PathVariable Long id) {
        permissionChecker.checkEdit(menuId);
        PurchaseDto approved = purchaseService.approvePurchase(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_purchase_approved", approved, status.value()));
    }

    @PatchMapping("/{id}/void")
    public ResponseEntity<ApiResponse> voidInvoice(@PathVariable Long id) {
        permissionChecker.checkEdit(menuId);
        PurchaseDto voided = purchaseService.voidPurchase(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_purchase_voided", voided, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        purchaseService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_purchase_deleted", null, status.value()));
    }
}
