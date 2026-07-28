package com.mtalaat.restaurant.modules.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemAddOnDto {
    private Long id;
    private Long addOnId;
    private String addOnName;
    private Double price;
}
