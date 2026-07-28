package com.mtalaat.restaurant.modules.order.dto;

import com.mtalaat.restaurant.modules.order.enums.OrderStatus;
import com.mtalaat.restaurant.modules.order.enums.OrderType;
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
public class OrderDto {
    private Long id;
    private String orderNumber;
    private OrderType orderType;
    private OrderStatus status;
    private String customerType;
    private Long customerId;
    private String customerName;
    private Long thirdPartyCustomerId;
    private String thirdPartyCustomerName;
    private Long tableId;
    private String tableName;
    private Long waiterId;
    private String waiterName;
    private Long cashRegisterId;
    private Double totalAmount;
    private String notes;
    private Long parentOrderId;
    private List<OrderItemDto> orderItems;
    private List<KitchenOrderDto> kitchenOrders;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
}
