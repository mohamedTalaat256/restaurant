package com.mtalaat.restaurant.modules.order.dto;

import com.mtalaat.restaurant.modules.order.enums.OrderType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {

    @NotNull(message = "Order type is required")
    private OrderType orderType;

    private String customerType;

    private Long customerId;

    private Long thirdPartyCustomerId;

    /**
     * Required for PLACE_ORDER, optional for QUICK_ORDER.
     */
    private Long tableId;

    private Long waiterId;

    private Long cashRegisterId;

    private String notes;

    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemRequestDto> orderItems;
}
