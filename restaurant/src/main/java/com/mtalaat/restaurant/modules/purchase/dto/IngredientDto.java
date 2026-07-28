package com.mtalaat.restaurant.modules.purchase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Unit of measurement is required")
    private Long uomId;

    private String uomName;

    @NotNull(message = "Stock quantity is required")
    private Double stockQuantity;

    @NotNull(message = "Min stock quantity is required")
    private Double minStockQuantity;

    private Boolean status;
}
