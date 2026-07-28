package com.mtalaat.restaurant.modules.settings.mapping;

import com.mtalaat.restaurant.modules.settings.dto.UnitOfMeasurementDto;
import com.mtalaat.restaurant.modules.settings.entity.UnitOfMeasurement;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasurementMapper {

    public UnitOfMeasurementDto toDto(UnitOfMeasurement entity) {
        return UnitOfMeasurementDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .shortName(entity.getShortName())
                .status(entity.getStatus())
                .build();
    }

    public UnitOfMeasurement toEntity(UnitOfMeasurementDto dto) {
        return UnitOfMeasurement.builder()
                .name(dto.getName())
                .shortName(dto.getShortName())
                .status(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus())
                .build();
    }
}
