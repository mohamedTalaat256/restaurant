package com.mtalaat.restaurant.modules.delivery.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Payload sent by the driver app to record a new GPS position.
 */
public record UpdateDriverLocationRequest(

        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
        @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
        BigDecimal latitude,

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
        @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
        BigDecimal longitude,

        /** Speed in km/h. Optional. */
        Double speed,

        /** Heading in degrees (0–360). Optional. */
        Double heading
) {}
