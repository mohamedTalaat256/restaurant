package com.mtalaat.restaurant.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDto {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String pageUrl;
    private Boolean isPage;
    private Long moduleId;
    private Long parentMenuId;
    private Boolean isReport;
    private Set<RolePermissionDto> rolePermissions;
}
