package com.mtalaat.restaurant.modules.foodManagement.mapping;

import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodAddOnsDto;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFoodAddOns;
import org.springframework.stereotype.Component;

@Component
public class ItemFoodAddOnsMapper {

    public ItemFoodAddOnsDto toDto(ItemFoodAddOns entity) {
        return ItemFoodAddOnsDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .build();
    }

    public ItemFoodAddOns toEntity(ItemFoodAddOnsDto dto) {
        ItemFoodAddOns entity = new ItemFoodAddOns();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());
        return entity;
    }
}
