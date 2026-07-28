package com.mtalaat.restaurant.modules.order.dto;

import com.mtalaat.restaurant.modules.order.enums.KitchenOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KitchenOrderDto {
    private Long id;
    private Long orderId;
    private String orderNumber;
    private Long kitchenId;
    private String kitchenName;
    private KitchenOrderStatus status;
    private String notes;
    private List<KitchenOrderItemDto> kitchenOrderItems;
    private LocalDateTime createdAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime preparedAt;
    private LocalDateTime readyAt;
}
