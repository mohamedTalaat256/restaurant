package com.mtalaat.restaurant.modules.foodManagement.mapping;

import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodAddOnsAssociationDto;
import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodDto;
import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodVariantDto;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemCategory;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFood;
import com.mtalaat.restaurant.modules.foodManagement.entity.MenuType;
import com.mtalaat.restaurant.modules.settings.entity.Kitchen;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemFoodMapper {

    public ItemFoodDto toDto(ItemFood entity) {

        return ItemFoodDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .descrip(entity.getDescrip())
                .component(entity.getComponent())
                .note(entity.getNote())
                .image(entity.getImage())
                .position(entity.getPosition())
                .isGroup(entity.getIsGroup())
                .cookedTime(entity.getCookedTime())
                .offerIsAvailable(entity.getOfferIsAvailable())
                .offerRate(entity.getOfferRate())
                .offerStartDate(entity.getOfferStartDate())
                .offerEndDate(entity.getOfferEndDate())
                .status(entity.getStatus())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
                .kitchenId(entity.getKitchen() != null ? entity.getKitchen().getId() : null)
                .kitchenName(entity.getKitchen() != null ? entity.getKitchen().getName() : null)
                .isCustomQty(entity.getIsCustomQty())
                .isSpecial(entity.getIsSpecial())
                .productVat(entity.getProductVat())
                .tax0(entity.getTax0())
                .tax1(entity.getTax1())
                .menuTypeId(entity.getMenuType() != null ? entity.getMenuType().getId() : null)
                .menuTypeName(entity.getMenuType() != null ? entity.getMenuType().getName() : null)
                .variants(entity.getVariants() != null ? entity.getVariants().stream()
                        .map(variant -> ItemFoodVariantDto.builder()
                                .id(variant.getId())
                                .name(variant.getName())
                                .price(variant.getPrice())
                                .build())
                        .toList() : List.of())
                .addOnsAssociations(entity.getAddOnsAssociations()!= null
                        ? entity.getAddOnsAssociations().stream().map(
                                addOnsAssociation -> ItemFoodAddOnsAssociationDto.builder()
                                        .itemFoodId(addOnsAssociation.getItemFood().getId())
                                        .itemFoodName(addOnsAssociation.getItemFood().getName())
                                        .itemFoodAddOnsId(addOnsAssociation.getItemFoodAddOns().getId())
                                        .itemFoodAddOnsName(addOnsAssociation.getItemFoodAddOns().getName())
                                        .itemFoodAddOnsPrice(addOnsAssociation.getItemFoodAddOns().getPrice())
                                        .build()

                        ).toList() : List.of())

                .build();
    }

    public ItemFood toEntity(ItemFoodDto dto, ItemCategory category, Kitchen kitchen, MenuType menuType) {
        ItemFood entity = new ItemFood();
        entity.setName(dto.getName());
        entity.setDescrip(dto.getDescrip());
        entity.setComponent(dto.getComponent());
        entity.setNote(dto.getNote());
        entity.setImage(dto.getImage());
        entity.setPosition(dto.getPosition());
        entity.setIsGroup(dto.getIsGroup() == null ? Boolean.FALSE : dto.getIsGroup());
        entity.setCookedTime(dto.getCookedTime());
        entity.setOfferIsAvailable(dto.getOfferIsAvailable() == null ? Boolean.FALSE : dto.getOfferIsAvailable());
        entity.setOfferRate(dto.getOfferRate());
        entity.setOfferStartDate(dto.getOfferStartDate());
        entity.setOfferEndDate(dto.getOfferEndDate());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());
        entity.setCategory(category);
        entity.setKitchen(kitchen);
        entity.setIsCustomQty(dto.getIsCustomQty() == null ? Boolean.FALSE : dto.getIsCustomQty());
        entity.setIsSpecial(dto.getIsSpecial() == null ? Boolean.FALSE : dto.getIsSpecial());
        entity.setProductVat(dto.getProductVat());
        entity.setTax0(dto.getTax0());
        entity.setTax1(dto.getTax1());
        entity.setMenuType(menuType);
        return entity;
    }
}
