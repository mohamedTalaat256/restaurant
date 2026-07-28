package com.mtalaat.restaurant.modules.purchase.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

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
}
