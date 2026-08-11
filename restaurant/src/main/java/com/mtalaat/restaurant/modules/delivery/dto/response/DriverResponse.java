package com.mtalaat.restaurant.modules.delivery.dto.response;

import com.mtalaat.restaurant.modules.delivery.enums.DriverStatus;
import com.mtalaat.restaurant.modules.delivery.enums.VehicleType;

import java.time.LocalDateTime;

/**
 * Driver profile details returned to clients.
 */
public record DriverResponse(
        Long id,
        Long userId,
        String fullName,
        String email,
        String phone,
        VehicleType vehicleType,
        String vehiclePlate,
        Boolean isOnline,
        DriverStatus status,
        LocalDateTime createdAt
) {}
