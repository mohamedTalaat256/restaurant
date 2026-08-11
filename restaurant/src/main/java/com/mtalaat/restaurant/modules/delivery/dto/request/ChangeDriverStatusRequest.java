package com.mtalaat.restaurant.modules.delivery.dto.request;

import com.mtalaat.restaurant.modules.delivery.enums.DriverStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request body to change a driver's availability status.
 */
public record ChangeDriverStatusRequest(

        @NotNull(message = "Status is required")
        DriverStatus status
) {}
