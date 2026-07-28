package com.mtalaat.restaurant.modules.settings.mapping;

import com.mtalaat.restaurant.modules.settings.dto.ThirdPartyCustomerDto;
import com.mtalaat.restaurant.modules.settings.entity.ThirdPartyCustomer;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartyCustomerMapper {

    public ThirdPartyCustomerDto toDto(ThirdPartyCustomer entity) {
        return ThirdPartyCustomerDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .commissionPercentage(entity.getCommissionPercentage())
                .build();
    }

    public ThirdPartyCustomer toEntity(ThirdPartyCustomerDto dto) {
        return ThirdPartyCustomer.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .commissionPercentage(dto.getCommissionPercentage())
                .build();
    }
}
