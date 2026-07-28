package com.mtalaat.restaurant.modules.foodManagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemFoodDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String descrip;

    private String component;

    private String note;

    private String image;

    private Integer position;

    private Boolean isGroup;

    private Integer cookedTime;

    private Boolean offerIsAvailable;

    private Double offerRate;

    private LocalDate offerStartDate;

    private LocalDate offerEndDate;

    private Boolean status;

    private Long categoryId;

    private String categoryName;

    private Long kitchenId;

    private String kitchenName;

    private Boolean isCustomQty;

    private Boolean isSpecial;

    private Double productVat;

    private Double tax0;

    private Double tax1;

    private Long menuTypeId;

    private String menuTypeName;

    private List<ItemFoodVariantDto> variants;
    private List<ItemFoodAddOnsAssociationDto> addOnsAssociations;
}
