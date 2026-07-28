package com.mtalaat.restaurant.modules.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloseCashRegisterDto {

    @NotNull(message = "Closing balance is required")
    private Double closingBalance;

    private String closingNote;
}
