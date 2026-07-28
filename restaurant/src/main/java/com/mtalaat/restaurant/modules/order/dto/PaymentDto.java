package com.mtalaat.restaurant.modules.order.dto;

import com.mtalaat.restaurant.modules.order.enums.PaymentMethod;
import com.mtalaat.restaurant.modules.order.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private Long orderId;
    private String orderNumber;
    private PaymentMethod paymentMethod;
    private Double totalAmount;
    private Double paidAmount;
    private Double remainingAmount;
    private Double changeAmount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}
