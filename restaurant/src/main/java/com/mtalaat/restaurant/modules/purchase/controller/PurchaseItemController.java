package com.mtalaat.restaurant.modules.purchase.controller;

import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.purchase.dto.PurchaseItemDto;
import com.mtalaat.restaurant.modules.purchase.service.PurchaseItemService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase/purchase-items")
@RequiredArgsConstructor
public class PurchaseItemController {

    private final PurchaseItemService purchaseItemService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.purchase-item-id}")
    private Long menuId;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody PurchaseItemDto dto) {
        permissionChecker.checkCreate(menuId);
        PurchaseItemDto created = purchaseItemService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_purchase_item_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        PurchaseItemDto purchaseItem = purchaseItemService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_purchase_item_fetched", purchaseItem, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_purchase_item_fetched", purchaseItemService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody PurchaseItemDto dto) {
        permissionChecker.checkEdit(menuId);
        PurchaseItemDto updated = purchaseItemService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_purchase_item_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        purchaseItemService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_purchase_item_deleted", null, status.value()));
    }
}
