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
public class CashCounterDto {

    private Long id;

    @NotNull(message = "Number is required")
    private Integer number;
}
