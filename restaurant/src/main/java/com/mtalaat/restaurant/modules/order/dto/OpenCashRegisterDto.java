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
public class OpenCashRegisterDto {

    @NotNull(message = "Cash counter is required")
    private Long cashCounterId;

    @NotNull(message = "Opening balance is required")
    private Double openingBalance;

    private String openingNote;
}
