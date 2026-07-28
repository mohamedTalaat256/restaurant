package com.mtalaat.restaurant.modules.foodManagement.mapping;

import com.mtalaat.restaurant.modules.foodManagement.dto.ItemCategoryDto;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemCategory;
import org.springframework.stereotype.Component;

@Component
public class ItemCategoryMapper {

    public ItemCategoryDto toDto(ItemCategory entity) {
        return ItemCategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .image(entity.getImage())
                .position(entity.getPosition())
                .isOffer(entity.getIsOffer())
                .offerStartDate(entity.getOfferStartDate())
                .offerEndDate(entity.getOfferEndDate())
                .status(entity.getStatus())
                .build();
    }

    public ItemCategory toEntity(ItemCategoryDto dto) {
        ItemCategory entity = new ItemCategory();
        entity.setName(dto.getName());
        entity.setImage(dto.getImage());
        entity.setPosition(dto.getPosition());
        entity.setIsOffer(dto.getIsOffer() == null ? Boolean.FALSE : dto.getIsOffer());
        entity.setOfferStartDate(dto.getOfferStartDate());
        entity.setOfferEndDate(dto.getOfferEndDate());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());
        return entity;
    }
}
