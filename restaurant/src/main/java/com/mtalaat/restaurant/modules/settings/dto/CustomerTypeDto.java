package com.mtalaat.restaurant.modules.settings.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTypeDto {

    @NotBlank(message = "Type is required")
    private String type;

    private String description;

    @NotNull(message = "Ordering is required")
    private Integer ordering;

    private Boolean status;
}
