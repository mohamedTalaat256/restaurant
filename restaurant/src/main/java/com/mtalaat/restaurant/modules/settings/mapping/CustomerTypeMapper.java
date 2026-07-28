package com.mtalaat.restaurant.modules.settings.mapping;

import com.mtalaat.restaurant.modules.settings.dto.CustomerTypeDto;
import com.mtalaat.restaurant.modules.settings.entity.CustomerType;
import org.springframework.stereotype.Component;

@Component
public class CustomerTypeMapper {

    public CustomerTypeDto toDto(CustomerType entity) {
        return CustomerTypeDto.builder()
                .type(entity.getType())
                .description(entity.getDescription())
                .ordering(entity.getOrdering())
                .status(entity.getStatus())
                .build();
    }

    public CustomerType toEntity(CustomerTypeDto dto) {
        return CustomerType.builder()
                .type(dto.getType())
                .description(dto.getDescription())
                .ordering(dto.getOrdering())
                .status(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus())
                .build();
    }
}
