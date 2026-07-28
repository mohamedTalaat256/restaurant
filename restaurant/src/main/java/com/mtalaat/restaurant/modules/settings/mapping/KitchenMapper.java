package com.mtalaat.restaurant.modules.settings.mapping;

import com.mtalaat.restaurant.modules.settings.dto.KitchenDto;
import com.mtalaat.restaurant.modules.settings.entity.Kitchen;
import org.springframework.stereotype.Component;

@Component
public class KitchenMapper {

    public KitchenDto toDto(Kitchen entity) {
        return KitchenDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .ipAddress(entity.getIpAddress())
                .port(entity.getPort())
                .status(entity.getStatus())
                .build();
    }

    public Kitchen toEntity(KitchenDto dto) {
        return Kitchen.builder()
                .name(dto.getName())
                .ipAddress(dto.getIpAddress())
                .port(dto.getPort())
                .status(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus())
                .build();
    }
}
