package com.mtalaat.restaurant.modules.delivery.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * Request body to reassign a delivery to a different driver.
 * A mandatory reason is required for auditability.
 */
public record ReassignDriverRequest(

        @NotNull(message = "New driver ID is required")
        Long newDriverId,

        @NotNull(message = "Reason for reassignment is required")
        String reason
) {}
