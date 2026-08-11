package com.mtalaat.restaurant.modules.delivery.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request body to create a new delivery record for an existing order.
 */
public record CreateDeliveryRequest(

        @NotNull(message = "Order ID is required")
        Long orderId,

        @DecimalMin(value = "0.0", message = "Delivery fee must be non-negative")
        BigDecimal deliveryFee,

        LocalDateTime estimatedDeliveryTime,

        String notes
) {}
