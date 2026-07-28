package com.mtalaat.restaurant.modules.auth.mapping;

import com.mtalaat.restaurant.modules.auth.dto.ModulePermissionDto;
import com.mtalaat.restaurant.modules.auth.entity.ModulePermission;
import org.springframework.stereotype.Component;

@Component
public class ModulePermissionMapper {

    public ModulePermissionDto toDto(ModulePermission entity) {
        return ModulePermissionDto.builder()
                .id(entity.getId())
                .moduleId(entity.getModule().getId())
                .userId(entity.getUser().getId())
                .canCreate(entity.getCanCreate())
                .canRead(entity.getCanRead())
                .canUpdate(entity.getCanUpdate())
                .canDelete(entity.getCanDelete())
                .build();
    }
}
