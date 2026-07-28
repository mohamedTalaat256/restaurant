package com.mtalaat.restaurant.modules.order.mapping;

import com.mtalaat.restaurant.modules.order.dto.KitchenOrderDto;
import com.mtalaat.restaurant.modules.order.dto.OrderDto;
import com.mtalaat.restaurant.modules.order.dto.OrderItemDto;
import com.mtalaat.restaurant.modules.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;
    private final KitchenOrderMapper kitchenOrderMapper;

    public OrderDto toDto(Order entity) {
        List<OrderItemDto> itemDtos = entity.getOrderItems() == null
                ? Collections.emptyList()
                : entity.getOrderItems().stream().map(orderItemMapper::toDto).toList();

        List<KitchenOrderDto> kitchenOrderDtos = entity.getKitchenOrders() == null
                ? Collections.emptyList()
                : entity.getKitchenOrders().stream().map(kitchenOrderMapper::toDto).toList();

        return OrderDto.builder()
                .id(entity.getId())
                .orderNumber(entity.getOrderNumber())
                .orderType(entity.getOrderType())
                .status(entity.getStatus())
                .customerType(entity.getCustomerType())
                .customerId(entity.getCustomer() != null ? entity.getCustomer().getId() : null)
                .customerName(entity.getCustomer() != null ? entity.getCustomer().getName() : null)
                .thirdPartyCustomerId(entity.getThirdPartyCustomer() != null ? entity.getThirdPartyCustomer().getId() : null)
                .thirdPartyCustomerName(entity.getThirdPartyCustomer() != null ? entity.getThirdPartyCustomer().getName() : null)
                .tableId(entity.getTable() != null ? entity.getTable().getId() : null)
                .tableName(entity.getTable() != null ? entity.getTable().getName() : null)
                .waiterId(entity.getWaiter() != null ? entity.getWaiter().getId() : null)
                .waiterName(entity.getWaiter() != null
                        ? entity.getWaiter().getFirstname() + " " + entity.getWaiter().getLastname()
                        : null)
                .cashRegisterId(entity.getCashRegister() != null ? entity.getCashRegister().getId() : null)
                .totalAmount(entity.getTotalAmount())
                .notes(entity.getNotes())
                .parentOrderId(entity.getParentOrderId())
                .orderItems(itemDtos)
                .kitchenOrders(kitchenOrderDtos)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }
}
