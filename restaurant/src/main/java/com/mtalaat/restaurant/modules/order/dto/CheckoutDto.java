package com.mtalaat.restaurant.modules.order.dto;

import com.mtalaat.restaurant.modules.order.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutDto {

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Paid amount is required")
    @Positive(message = "Paid amount must be positive")
    private Double paidAmount;
}
