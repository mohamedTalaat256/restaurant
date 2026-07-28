package com.mtalaat.restaurant.modules.foodManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemFoodAddOnsDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    private Double price;

    private Boolean status;
}
