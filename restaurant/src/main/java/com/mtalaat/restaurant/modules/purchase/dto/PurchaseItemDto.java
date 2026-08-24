package com.mtalaat.restaurant.modules.purchase.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemDto {

    private Long id;

    @NotNull(message = "Ingredient is required")
    private Long ingredientId;

    private String ingredientName;

    @NotNull(message = "Quantity is required")
    private Double quantity;

    @NotNull(message = "Price is required")
    private Double price;

    private LocalDate productionDate;

    private LocalDate expiryDate;
}
