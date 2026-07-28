package com.mtalaat.restaurant.modules.order.entity;

import com.mtalaat.restaurant.modules.order.enums.KitchenOrderStatus;
import com.mtalaat.restaurant.modules.settings.entity.Kitchen;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kitchen_orders")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class KitchenOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kitchen_id", nullable = false)
    private Kitchen kitchen;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private KitchenOrderStatus status = KitchenOrderStatus.PENDING;

    @Column(name = "notes")
    private String notes;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "prepared_at")
    private LocalDateTime preparedAt;

    @Column(name = "ready_at")
    private LocalDateTime readyAt;

    @Builder.Default
    @OneToMany(mappedBy = "kitchenOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KitchenOrderItem> kitchenOrderItems = new ArrayList<>();
}
