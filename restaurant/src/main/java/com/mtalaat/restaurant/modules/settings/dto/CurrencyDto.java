package com.mtalaat.restaurant.modules.settings.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {

    private Long id;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Symbol is required")
    private String symbol;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Exchange rate to USD is required")
    private Double exchangeRateToUSD;
}
