package com.mtalaat.restaurant.modules.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {

    @NotNull(message = "Item food ID is required")
    private Long itemFoodId;

    @NotNull(message = "Price is required")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private Long variantId;

    private String variantName;

    private List<Long> addOnIds;

    private Double addOnsPrice;

    private String notes;
}
