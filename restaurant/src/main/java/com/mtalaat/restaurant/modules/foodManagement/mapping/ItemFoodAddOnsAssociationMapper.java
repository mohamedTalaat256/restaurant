package com.mtalaat.restaurant.modules.foodManagement.mapping;

import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodAddOnsAssociationDto;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFood;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFoodAddOns;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFoodAddOnsAssociation;
import org.springframework.stereotype.Component;

@Component
public class ItemFoodAddOnsAssociationMapper {

    public ItemFoodAddOnsAssociationDto toDto(ItemFoodAddOnsAssociation entity) {
        return ItemFoodAddOnsAssociationDto.builder()
                .id(entity.getId())
                .itemFoodId(entity.getItemFood() != null ? entity.getItemFood().getId() : null)
                .itemFoodName(entity.getItemFood() != null ? entity.getItemFood().getName() : null)
                .itemFoodAddOnsId(entity.getItemFoodAddOns() != null ? entity.getItemFoodAddOns().getId() : null)
                .itemFoodAddOnsName(entity.getItemFoodAddOns() != null ? entity.getItemFoodAddOns().getName() : null)
                .build();
    }

    public ItemFoodAddOnsAssociation toEntity(ItemFoodAddOnsAssociationDto dto, ItemFood itemFood, ItemFoodAddOns itemFoodAddOns) {
        ItemFoodAddOnsAssociation entity = new ItemFoodAddOnsAssociation();
        entity.setItemFood(itemFood);
        entity.setItemFoodAddOns(itemFoodAddOns);
        return entity;
    }
}
