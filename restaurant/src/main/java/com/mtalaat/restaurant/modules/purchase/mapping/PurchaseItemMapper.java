package com.mtalaat.restaurant.modules.purchase.mapping;

import com.mtalaat.restaurant.modules.purchase.dto.PurchaseItemDto;
import com.mtalaat.restaurant.modules.purchase.entity.Ingredient;
import com.mtalaat.restaurant.modules.purchase.entity.PurchaseItem;
import com.mtalaat.restaurant.modules.purchase.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PurchaseItemMapper {

    private final IngredientRepository ingredientRepository;

    public PurchaseItemDto toDto(PurchaseItem entity) {
        return PurchaseItemDto.builder()
                .id(entity.getId())
                .ingredientId(entity.getIngredient() != null ? entity.getIngredient().getId() : null)
                .ingredientName(entity.getIngredient() != null ? entity.getIngredient().getName() : null)
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .productionDate(entity.getProductionDate())
                .expiryDate(entity.getExpiryDate())
                .build();
    }

    public PurchaseItem toEntity(PurchaseItemDto dto) {

        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId()).orElseThrow();

        PurchaseItem entity = new PurchaseItem();
        entity.setIngredient(ingredient);
        entity.setQuantity(dto.getQuantity());
        entity.setPrice(dto.getPrice());
        entity.setProductionDate(dto.getProductionDate());
        entity.setExpiryDate(dto.getExpiryDate());
        return entity;
    }
}
