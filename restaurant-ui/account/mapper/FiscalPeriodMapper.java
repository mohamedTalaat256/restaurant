package com.mtalaat.restaurant.modules.account.mapper;

import com.mtalaat.restaurant.modules.account.dto.FiscalPeriodDTO;
import com.mtalaat.restaurant.modules.account.entity.FiscalPeriod;
import org.springframework.stereotype.Component;

@Component
public class FiscalPeriodMapper {

    public FiscalPeriodDTO toDTO(FiscalPeriod entity) {
        if (entity == null) return null;
        return FiscalPeriodDTO.builder()
                .id(entity.getId())
                .year(entity.getYear())
                .month(entity.getMonth())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .locked(entity.getLocked())
                .lockedAt(entity.getLockedAt())
                .lockedByUsername(entity.getLockedByUsername())
                .build();
    }
}
