package com.mtalaat.restaurant.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDto {

    private Long id;

    @NotBlank(message = "Module name is required")
    private String name;

    private String description;
    private String image;
    private Boolean status;
}
