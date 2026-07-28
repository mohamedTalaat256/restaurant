package com.mtalaat.restaurant.modules.order.mapping;

import com.mtalaat.restaurant.modules.order.dto.KitchenOrderDto;
import com.mtalaat.restaurant.modules.order.dto.KitchenOrderItemDto;
import com.mtalaat.restaurant.modules.order.entity.KitchenOrder;
import com.mtalaat.restaurant.modules.order.entity.KitchenOrderItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class KitchenOrderMapper {

    public KitchenOrderDto toDto(KitchenOrder entity) {
        List<KitchenOrderItemDto> itemDtos = entity.getKitchenOrderItems() == null
                ? Collections.emptyList()
                : entity.getKitchenOrderItems().stream().map(this::toItemDto).toList();

        return KitchenOrderDto.builder()
                .id(entity.getId())
                .orderId(entity.getOrder() != null ? entity.getOrder().getId() : null)
                .orderNumber(entity.getOrder() != null ? entity.getOrder().getOrderNumber() : null)
                .kitchenId(entity.getKitchen() != null ? entity.getKitchen().getId() : null)
                .kitchenName(entity.getKitchen() != null ? entity.getKitchen().getName() : null)
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .kitchenOrderItems(itemDtos)
                .createdAt(entity.getCreatedAt())
                .acceptedAt(entity.getAcceptedAt())
                .rejectedAt(entity.getRejectedAt())
                .preparedAt(entity.getPreparedAt())
                .readyAt(entity.getReadyAt())
                .build();
    }

    public KitchenOrderItemDto toItemDto(KitchenOrderItem entity) {
        return KitchenOrderItemDto.builder()
                .id(entity.getId())
                .orderItemId(entity.getOrderItem() != null ? entity.getOrderItem().getId() : null)
                .itemFoodName(entity.getOrderItem() != null ? entity.getOrderItem().getItemFoodName() : null)
                .quantity(entity.getOrderItem() != null ? entity.getOrderItem().getQuantity() : null)
                .variantName(entity.getOrderItem() != null ? entity.getOrderItem().getVariantName() : null)
                .addOnsPrice(entity.getOrderItem() != null ? entity.getOrderItem().getAddOnsPrice() : null)
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .build();
    }
}
