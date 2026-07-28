package com.mtalaat.restaurant.modules.auth.mapping;

import com.mtalaat.restaurant.modules.auth.dto.RolePermissionDto;
import com.mtalaat.restaurant.modules.auth.entity.RolePermission;
import org.springframework.stereotype.Component;

@Component
public class RolePermissionMapper {

    public RolePermissionDto toDto(RolePermission entity) {
        return RolePermissionDto.builder()
                .id(entity.getId())
                .roleId(entity.getRole().getId())
                .menuItemId(entity.getMenuItem() != null ? entity.getMenuItem().getId() : null)
                .canRead(entity.getCanRead())
                .canCreate(entity.getCanCreate())
                .canEdit(entity.getCanEdit())
                .canDelete(entity.getCanDelete())
                .build();
    }
}
