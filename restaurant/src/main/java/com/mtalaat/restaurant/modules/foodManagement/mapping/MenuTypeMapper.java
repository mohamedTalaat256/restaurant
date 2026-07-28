package com.mtalaat.restaurant.modules.foodManagement.mapping;

import com.mtalaat.restaurant.modules.foodManagement.dto.MenuTypeDto;
import com.mtalaat.restaurant.modules.foodManagement.entity.MenuType;
import org.springframework.stereotype.Component;

@Component
public class MenuTypeMapper {

    public MenuTypeDto toDto(MenuType entity) {
        return MenuTypeDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .image(entity.getImage())
                .status(entity.getStatus())
                .build();
    }

    public MenuType toEntity(MenuTypeDto dto) {
        MenuType entity = new MenuType();
        entity.setName(dto.getName());
        entity.setImage(dto.getImage());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());
        return entity;
    }
}
