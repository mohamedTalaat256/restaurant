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
public class RoleDto {

    private Long id;

    @NotBlank(message = "Role name is required")
    private String name;

    private String description;
    private Boolean status;
}
