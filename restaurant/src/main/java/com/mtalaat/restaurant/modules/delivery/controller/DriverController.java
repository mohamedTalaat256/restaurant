package com.mtalaat.restaurant.modules.delivery.controller;

import com.mtalaat.restaurant.modules.delivery.dto.request.ChangeDriverStatusRequest;
import com.mtalaat.restaurant.modules.delivery.service.DriverService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse> getAllDrivers() {
        var results = driverService.getAllDrivers();
        return ResponseEntity.ok(ApiResponse.success("msg_drivers_fetched", results, HttpStatus.OK.value()));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse> getAvailableDrivers() {
        var results = driverService.getAvailableDrivers();
        return ResponseEntity.ok(ApiResponse.success("msg_drivers_fetched", results, HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDriverById(@PathVariable Long id) {
        var result = driverService.getDriverById(id);
        return ResponseEntity.ok(ApiResponse.success("msg_driver_fetched", result, HttpStatus.OK.value()));
    }

    // ─────────────────────────────────────────────
    // STATUS
    // ─────────────────────────────────────────────

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse> changeDriverStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeDriverStatusRequest request) {
        var result = driverService.changeDriverStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("msg_driver_status_updated", result, HttpStatus.OK.value()));
    }

    // ─────────────────────────────────────────────
    // CURRENT DELIVERY
    // ─────────────────────────────────────────────

    @GetMapping("/{id}/current-delivery")
    public ResponseEntity<ApiResponse> getCurrentDelivery(@PathVariable Long id) {
        var result = driverService.getDriverCurrentDelivery(id);
        return ResponseEntity.ok(ApiResponse.success("msg_delivery_fetched", result, HttpStatus.OK.value()));
    }

    // ─────────────────────────────────────────────
    // DELIVERY HISTORY
    // ─────────────────────────────────────────────

    @GetMapping("/{id}/deliveries")
    public ResponseEntity<ApiResponse> getDeliveryHistory(@PathVariable Long id) {
        var results = driverService.getDriverDeliveryHistory(id);
        return ResponseEntity.ok(ApiResponse.success("msg_deliveries_fetched", results, HttpStatus.OK.value()));
    }
}
