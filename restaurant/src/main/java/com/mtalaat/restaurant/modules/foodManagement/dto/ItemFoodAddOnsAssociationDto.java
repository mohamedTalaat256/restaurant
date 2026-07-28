package com.mtalaat.restaurant.modules.foodManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemFoodAddOnsAssociationDto {

    private Long id;

    private Long itemFoodId;

    private String itemFoodName;

    private Long itemFoodAddOnsId;

    private String itemFoodAddOnsName;

    private Double itemFoodAddOnsPrice;
}
