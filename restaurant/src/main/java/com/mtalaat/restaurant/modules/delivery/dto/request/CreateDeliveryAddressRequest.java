package com.mtalaat.restaurant.modules.delivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request body for saving a new delivery address to a customer's address book.
 */
public record CreateDeliveryAddressRequest(

        @NotNull(message = "Customer ID is required")
        Long customerId,

        String label,

        @NotBlank(message = "Street address is required")
        String streetAddress,

        @NotBlank(message = "City is required")
        String city,

        String district,

        String buildingNumber,

        String floorNumber,

        String apartmentNumber,

        BigDecimal latitude,

        BigDecimal longitude,

        String additionalNotes,

        boolean isDefault
) {}
