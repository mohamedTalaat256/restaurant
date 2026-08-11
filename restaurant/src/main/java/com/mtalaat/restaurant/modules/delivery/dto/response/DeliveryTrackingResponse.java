package com.mtalaat.restaurant.modules.delivery.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A single GPS tracking point for a delivery.
 */
public record DeliveryTrackingResponse(
        Long id,
        Long deliveryId,
        BigDecimal latitude,
        BigDecimal longitude,
        Double speed,
        Double heading,
        LocalDateTime createdAt
) {}
