package com.mtalaat.restaurant.modules.order.dto;

import com.mtalaat.restaurant.modules.order.enums.KitchenOrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KitchenOrderItemDto {
    private Long id;
    private Long orderItemId;
    private String itemFoodName;
    private Integer quantity;
    private String variantName;
    private Double addOnsPrice;
    private KitchenOrderItemStatus status;
    private String notes;
}
