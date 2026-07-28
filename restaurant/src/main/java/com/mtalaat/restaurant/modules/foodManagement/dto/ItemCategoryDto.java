package com.mtalaat.restaurant.modules.foodManagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCategoryDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String image;

    private Integer position;

    private Boolean isOffer;

    private LocalDate offerStartDate;

    private LocalDate offerEndDate;

    private Boolean status;
}
