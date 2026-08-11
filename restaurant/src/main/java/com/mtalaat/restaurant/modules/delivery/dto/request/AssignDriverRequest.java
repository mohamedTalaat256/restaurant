package com.mtalaat.restaurant.modules.delivery.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * Request body to assign a driver to an existing delivery.
 */
public record AssignDriverRequest(

        @NotNull(message = "Driver ID is required")
        Long driverId,

        String reason
) {}
