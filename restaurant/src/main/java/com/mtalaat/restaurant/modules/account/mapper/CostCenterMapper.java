package com.mtalaat.restaurant.modules.account.mapper;

import com.mtalaat.restaurant.modules.account.dto.CostCenterDTO;
import com.mtalaat.restaurant.modules.account.entity.CostCenter;
import org.springframework.stereotype.Component;

@Component
public class CostCenterMapper {

    public CostCenterDTO toDTO(CostCenter entity) {
        if (entity == null) return null;
        return CostCenterDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .status(entity.getStatus())
                .build();
    }

    public CostCenter toEntity(CostCenterDTO dto) {
        if (dto == null) return null;
        return CostCenter.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .status(dto.getStatus() != null ? dto.getStatus() : true)
                .build();
    }
}
