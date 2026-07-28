package com.mtalaat.restaurant.modules.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SplitOrderItemDto {

    @NotNull(message = "Order item ID is required")
    private Long orderItemId;

    /**
     * How many of this item's quantity to move to the split order.
     */
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
