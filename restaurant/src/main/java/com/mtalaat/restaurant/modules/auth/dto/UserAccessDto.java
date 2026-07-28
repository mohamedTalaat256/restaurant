package com.mtalaat.restaurant.modules.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccessDto {

    private Long id;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Role id is required")
    private Long roleId;
}
