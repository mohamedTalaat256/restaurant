package com.mtalaat.restaurant.modules.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutDto {

    @NotNull(message = "Paid amount is required")
    @Positive(message = "Paid amount must be positive")
    private BigDecimal paidAmount;

    private boolean isCash = true;
}
