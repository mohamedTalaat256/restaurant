package com.mtalaat.restaurant.modules.foodManagement.mapping;

import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodVariantDto;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFood;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFoodVariant;
import org.springframework.stereotype.Component;

@Component
public class ItemFoodVariantMapper {

    public ItemFoodVariantDto toDto(ItemFoodVariant entity) {
        return ItemFoodVariantDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .itemFoodId(entity.getItemFood() != null ? entity.getItemFood().getId() : null)
                .itemFoodName(entity.getItemFood() != null ? entity.getItemFood().getName() : null)
                .build();
    }

    public ItemFoodVariant toEntity(ItemFoodVariantDto dto, ItemFood itemFood) {
        ItemFoodVariant entity = new ItemFoodVariant();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setItemFood(itemFood);
        return entity;
    }
}
