package com.mtalaat.restaurant.modules.order.entity;

import com.mtalaat.restaurant.modules.order.enums.PaymentMethod;
import com.mtalaat.restaurant.modules.order.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "paid_amount", nullable = false)
    private Double paidAmount;

    @Column(name = "remaining_amount", nullable = false)
    private Double remainingAmount;

    @Column(name = "change_amount", nullable = false)
    private Double changeAmount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
