package com.mtalaat.restaurant.modules.purchase.mapping;

import com.mtalaat.restaurant.modules.purchase.dto.IngredientDto;
import com.mtalaat.restaurant.modules.purchase.entity.Ingredient;
import com.mtalaat.restaurant.modules.settings.entity.UnitOfMeasurement;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {

    public IngredientDto toDto(Ingredient entity) {
        return IngredientDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .uomId(entity.getUom() != null ? entity.getUom().getId() : null)
                .uomName(entity.getUom() != null ? entity.getUom().getName() : null)
                .stockQuantity(entity.getStockQuantity())
                .minStockQuantity(entity.getMinStockQuantity())
                .status(entity.getStatus())
                .build();
    }

    public Ingredient toEntity(IngredientDto dto, UnitOfMeasurement uom) {
        return Ingredient.builder()
                .name(dto.getName())
                .uom(uom)
                .stockQuantity(dto.getStockQuantity())
                .minStockQuantity(dto.getMinStockQuantity())
                .status(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus())
                .build();
    }
}
