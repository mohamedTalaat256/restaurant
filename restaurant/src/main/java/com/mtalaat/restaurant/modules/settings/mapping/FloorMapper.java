package com.mtalaat.restaurant.modules.settings.mapping;

import com.mtalaat.restaurant.modules.settings.dto.FloorDto;
import com.mtalaat.restaurant.modules.settings.entity.Floor;
import org.springframework.stereotype.Component;

@Component
public class FloorMapper {

    public FloorDto toDto(Floor entity) {
        return FloorDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public Floor toEntity(FloorDto dto) {
        return Floor.builder()
                .name(dto.getName())
                .build();
    }
}
