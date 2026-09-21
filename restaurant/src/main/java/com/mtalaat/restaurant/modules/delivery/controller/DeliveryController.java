package com.mtalaat.restaurant.modules.delivery.controller;

import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.delivery.dto.DeliveryDetailsDto;
import com.mtalaat.restaurant.modules.delivery.service.DeliveryService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.delivery-id}")
    private Long menuId;

    // ─────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody DeliveryDetailsDto dto) {
        permissionChecker.checkCreate(menuId);
        DeliveryDetailsDto created = deliveryService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_delivery_created", created, status.value()));
    }

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        DeliveryDetailsDto delivery = deliveryService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_delivery_fetched", delivery, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(@RequestParam(required = false) Boolean status) {
        permissionChecker.checkRead(menuId);
        var deliveries = status != null
                ? deliveryService.getByStatus(status)
                : deliveryService.getAll();
        HttpStatus httpStatus = HttpStatus.OK;
        return ResponseEntity.status(httpStatus)
                .body(ApiResponse.success("msg_deliveries_fetched", deliveries, httpStatus.value()));
    }

    // ─────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id,
                                              @Valid @RequestBody DeliveryDetailsDto dto) {
        permissionChecker.checkEdit(menuId);
        DeliveryDetailsDto updated = deliveryService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_delivery_updated", updated, status.value()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Long id,
                                                    @RequestParam Boolean status) {
        permissionChecker.checkEdit(menuId);
        DeliveryDetailsDto updated = deliveryService.updateStatus(id, status);
        HttpStatus httpStatus = HttpStatus.OK;
        return ResponseEntity.status(httpStatus)
                .body(ApiResponse.success("msg_delivery_status_updated", updated, httpStatus.value()));
    }

    // ─────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        deliveryService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_delivery_deleted", null, status.value()));
    }
}
