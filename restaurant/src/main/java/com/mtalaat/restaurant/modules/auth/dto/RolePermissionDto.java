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
public class RolePermissionDto {

    private Long id;

    @NotNull(message = "Role id is required")
    private Long roleId;

    private Long menuItemId;
    private Boolean canRead;
    private Boolean canCreate;
    private Boolean canEdit;
    private Boolean canDelete;
}
