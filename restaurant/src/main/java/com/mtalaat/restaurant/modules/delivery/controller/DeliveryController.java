package com.mtalaat.restaurant.modules.delivery.controller;

import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.delivery.dto.request.AssignDriverRequest;
import com.mtalaat.restaurant.modules.delivery.dto.request.CancelDeliveryRequest;
import com.mtalaat.restaurant.modules.delivery.dto.request.CreateDeliveryRequest;
import com.mtalaat.restaurant.modules.delivery.dto.request.ReassignDriverRequest;
import com.mtalaat.restaurant.modules.delivery.service.DeliveryService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final PermissionChecker permissionChecker;

    // ─────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ApiResponse> createDelivery(@Valid @RequestBody CreateDeliveryRequest request) {
        var result = deliveryService.createDelivery(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("msg_delivery_created", result, HttpStatus.CREATED.value()));
    }

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDeliveryById(@PathVariable Long id) {
        var result = deliveryService.getDeliveryById(id);
        return ResponseEntity.ok(ApiResponse.success("msg_delivery_fetched", result, HttpStatus.OK.value()));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse> getDeliveryByOrder(@PathVariable Long orderId) {
        var result = deliveryService.getDeliveryByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success("msg_delivery_fetched", result, HttpStatus.OK.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllDeliveries() {
        List<?> results = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(ApiResponse.success("msg_deliveries_fetched", results, HttpStatus.OK.value()));
    }

    // ─────────────────────────────────────────────
    // ASSIGNMENT
    // ─────────────────────────────────────────────

    @PostMapping("/{id}/assign")
    public ResponseEntity<ApiResponse> assignDriver(
            @PathVariable Long id,
            @Valid @RequestBody AssignDriverRequest request) {
        User currentUser = permissionChecker.getCurrentUser();
        var result = deliveryService.assignDriver(id, request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("msg_driver_assigned", result, HttpStatus.OK.value()));
    }

    @PostMapping("/{id}/reassign")
    public ResponseEntity<ApiResponse> reassignDriver(
            @PathVariable Long id,
            @Valid @RequestBody ReassignDriverRequest request) {
        User currentUser = permissionChecker.getCurrentUser();
        var result = deliveryService.reassignDriver(id, request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("msg_driver_reassigned", result, HttpStatus.OK.value()));
    }

    // ─────────────────────────────────────────────
    // STATE TRANSITIONS
    // ─────────────────────────────────────────────

    @PatchMapping("/{id}/accept")
    public ResponseEntity<ApiResponse> acceptDelivery(@PathVariable Long id) {
        var result = deliveryService.acceptDelivery(id);
        return ResponseEntity.ok(ApiResponse.success("msg_delivery_accepted", result, HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/arrived")
    public ResponseEntity<ApiResponse> arrivedAtRestaurant(@PathVariable Long id) {
        var result = deliveryService.arrivedAtRestaurant(id);
        return ResponseEntity.ok(ApiResponse.success("msg_delivery_arrived", result, HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/pickup")
    public ResponseEntity<ApiResponse> pickupOrder(@PathVariable Long id) {
        var result = deliveryService.pickupOrder(id);
        return ResponseEntity.ok(ApiResponse.success("msg_delivery_picked_up", result, HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<ApiResponse> startDelivery(@PathVariable Long id) {
        var result = deliveryService.startDelivery(id);
        return ResponseEntity.ok(ApiResponse.success("msg_delivery_started", result, HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse> completeDelivery(@PathVariable Long id) {
        var result = deliveryService.completeDelivery(id);
        return ResponseEntity.ok(ApiResponse.success("msg_delivery_completed", result, HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelDelivery(
            @PathVariable Long id,
            @RequestBody(required = false) CancelDeliveryRequest request) {
        var result = deliveryService.cancelDelivery(id, request != null ? request : new CancelDeliveryRequest(null));
        return ResponseEntity.ok(ApiResponse.success("msg_delivery_cancelled", result, HttpStatus.OK.value()));
    }
}
