package com.mtalaat.restaurant.modules.settings.dto;

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
public class TableDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Capacity is required")
    private Integer capacity;

    private String icon;

    @NotNull(message = "Floor is required")
    private Long floorId;

    private String floorName;

    private Boolean status;
}
