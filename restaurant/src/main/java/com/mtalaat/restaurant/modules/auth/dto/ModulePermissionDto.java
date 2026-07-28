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
public class ModulePermissionDto {

    private Long id;

    @NotNull(message = "Module id is required")
    private Long moduleId;

    @NotNull(message = "User id is required")
    private Long userId;

    private Boolean canCreate;
    private Boolean canRead;
    private Boolean canUpdate;
    private Boolean canDelete;
}
