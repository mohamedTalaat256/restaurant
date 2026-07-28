package com.mtalaat.restaurant.modules.order.mapping;

import com.mtalaat.restaurant.modules.order.dto.CashCounterDto;
import com.mtalaat.restaurant.modules.order.entity.CashCounter;
import org.springframework.stereotype.Component;

@Component
public class CashCounterMapper {

    public CashCounterDto toDto(CashCounter entity) {
        return CashCounterDto.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .build();
    }

    public CashCounter toEntity(CashCounterDto dto) {
        return CashCounter.builder()
                .number(dto.getNumber())
                .build();
    }
}
