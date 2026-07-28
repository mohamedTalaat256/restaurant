package com.mtalaat.restaurant.modules.settings.mapping;

import com.mtalaat.restaurant.modules.settings.dto.TableDto;
import com.mtalaat.restaurant.modules.settings.entity.Floor;
import com.mtalaat.restaurant.modules.settings.entity.RestaurantTable;
import org.springframework.stereotype.Component;

@Component
public class TableMapper {

    public TableDto toDto(RestaurantTable entity) {
        return TableDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .capacity(entity.getCapacity())
                .icon(entity.getIcon())
                .floorId(entity.getFloor() != null ? entity.getFloor().getId() : null)
                .floorName(entity.getFloor() != null ? entity.getFloor().getName() : null)
                .status(entity.getStatus())
                .build();
    }

    public RestaurantTable toEntity(TableDto dto, Floor floor) {
        return RestaurantTable.builder()
                .name(dto.getName())
                .capacity(dto.getCapacity())
                .icon(dto.getIcon())
                .floor(floor)
                .status(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus())
                .build();
    }
}
