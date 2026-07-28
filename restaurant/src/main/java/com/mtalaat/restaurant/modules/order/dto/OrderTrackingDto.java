package com.mtalaat.restaurant.modules.order.dto;

import com.mtalaat.restaurant.modules.order.enums.KitchenOrderItemStatus;
import com.mtalaat.restaurant.modules.order.enums.KitchenOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrackingDto {
    private Long orderId;
    private String orderNumber;
    private String overallStatus;
    private int totalItems;
    private int readyItems;
    private List<KitchenProgressDto> kitchenProgress;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KitchenProgressDto {
        private Long kitchenOrderId;
        private String kitchenName;
        private KitchenOrderStatus kitchenOrderStatus;
        private List<ItemProgressDto> items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemProgressDto {
        private Long kitchenOrderItemId;
        private String itemFoodName;
        private Integer quantity;
        private KitchenOrderItemStatus itemStatus;
    }
}
