package com.mtalaat.restaurant.modules.auth.mapping;

import com.mtalaat.restaurant.modules.auth.dto.ModuleDto;
import com.mtalaat.restaurant.modules.auth.entity.ModuleEntity;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper {

    public ModuleDto toDto(ModuleEntity entity) {
        return ModuleDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .image(entity.getIcon())
                .status(entity.getStatus())
                .build();
    }

    public ModuleEntity toEntity(ModuleDto dto) {
        ModuleEntity module = new ModuleEntity();
        module.setName(dto.getName());
        module.setDescription(dto.getDescription());
        module.setIcon(dto.getImage());
        module.setStatus(dto.getStatus());
        return module;
    }
}
