package com.mtalaat.restaurant.modules.delivery.controller;

import com.mtalaat.restaurant.modules.delivery.dto.request.UpdateDriverLocationRequest;
import com.mtalaat.restaurant.modules.delivery.service.DeliveryTrackingService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries/{deliveryId}/tracking")
@RequiredArgsConstructor
public class DeliveryTrackingController {

    private final DeliveryTrackingService trackingService;

    /**
     * Driver app sends periodic location updates.
     * POST /api/deliveries/{deliveryId}/tracking/location
     */
    @PostMapping("/location")
    public ResponseEntity<ApiResponse> updateLocation(
            @PathVariable Long deliveryId,
            @Valid @RequestBody UpdateDriverLocationRequest request) {
        var result = trackingService.recordLocation(deliveryId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("msg_location_recorded", result, HttpStatus.CREATED.value()));
    }

    /**
     * Returns the full GPS trail for a delivery.
     * GET /api/deliveries/{deliveryId}/tracking
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getDeliveryTracking(@PathVariable Long deliveryId) {
        var results = trackingService.getDeliveryTracking(deliveryId);
        return ResponseEntity.ok(ApiResponse.success("msg_tracking_fetched", results, HttpStatus.OK.value()));
    }

    /**
     * Returns only the driver's current (most recent) position.
     * GET /api/deliveries/{deliveryId}/tracking/latest
     */
    @GetMapping("/latest")
    public ResponseEntity<ApiResponse> getLatestLocation(@PathVariable Long deliveryId) {
        var result = trackingService.getLatestLocation(deliveryId);
        return ResponseEntity.ok(ApiResponse.success("msg_location_fetched", result, HttpStatus.OK.value()));
    }
}
