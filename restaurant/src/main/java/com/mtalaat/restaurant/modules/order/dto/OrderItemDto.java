package com.mtalaat.restaurant.modules.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long id;
    private Long itemFoodId;
    private String itemFoodName;
    private Double price;
    private Integer quantity;
    private Long variantId;
    private String variantName;
    private Double addOnsPrice;
    private Double totalPrice;
    private String notes;
    private List<OrderItemAddOnDto> orderItemAddOns;
}
