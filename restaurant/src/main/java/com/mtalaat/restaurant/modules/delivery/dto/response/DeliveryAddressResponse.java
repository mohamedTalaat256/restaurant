package com.mtalaat.restaurant.modules.delivery.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A customer's saved delivery address.
 */
public record DeliveryAddressResponse(
        Long id,
        Long customerId,
        String label,
        String streetAddress,
        String city,
        String district,
        String buildingNumber,
        String floorNumber,
        String apartmentNumber,
        BigDecimal latitude,
        BigDecimal longitude,
        String additionalNotes,
        Boolean isDefault,
        Boolean active,
        LocalDateTime createdAt
) {}
