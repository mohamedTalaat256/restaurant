package com.mtalaat.restaurant.modules.delivery.dto.response;

import com.mtalaat.restaurant.modules.delivery.enums.DeliveryStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Full delivery details returned to clients.
 */
public record DeliveryResponse(
        Long id,
        Long orderId,
        String orderNumber,
        DriverSummary driver,
        DeliveryStatus status,
        BigDecimal deliveryFee,
        LocalDateTime estimatedDeliveryTime,
        LocalDateTime actualDeliveryTime,
        LocalDateTime assignedAt,
        LocalDateTime acceptedAt,
        LocalDateTime pickedUpAt,
        LocalDateTime deliveredAt,
        LocalDateTime cancelledAt,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Embedded driver summary to avoid exposing full driver details in every delivery response.
     */
    public record DriverSummary(
            Long id,
            String fullName,
            String phone,
            String vehiclePlate,
            String vehicleType
    ) {}
}
