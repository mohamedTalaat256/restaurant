package com.mtalaat.restaurant.modules.order.mapping;

import com.mtalaat.restaurant.modules.order.dto.OrderItemAddOnDto;
import com.mtalaat.restaurant.modules.order.dto.OrderItemDto;
import com.mtalaat.restaurant.modules.order.entity.OrderItem;
import com.mtalaat.restaurant.modules.order.entity.OrderItemAddOn;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class OrderItemMapper {

    public OrderItemDto toDto(OrderItem entity) {
        List<OrderItemAddOnDto> addOnDtos = entity.getOrderItemAddOns() == null
                ? Collections.emptyList()
                : entity.getOrderItemAddOns().stream().map(this::toAddOnDto).toList();

        return OrderItemDto.builder()
                .id(entity.getId())
                .itemFoodId(entity.getItemFood() != null ? entity.getItemFood().getId() : null)
                .itemFoodName(entity.getItemFoodName())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .variantId(entity.getVariant() != null ? entity.getVariant().getId() : null)
                .variantName(entity.getVariantName())
                .addOnsPrice(entity.getAddOnsPrice())
                .totalPrice(entity.getTotalPrice())
                .notes(entity.getNotes())
                .orderItemAddOns(addOnDtos)
                .build();
    }

    public OrderItemAddOnDto toAddOnDto(OrderItemAddOn entity) {
        return OrderItemAddOnDto.builder()
                .id(entity.getId())
                .addOnId(entity.getAddOn() != null ? entity.getAddOn().getId() : null)
                .addOnName(entity.getAddOnName())
                .price(entity.getPrice())
                .build();
    }
}
