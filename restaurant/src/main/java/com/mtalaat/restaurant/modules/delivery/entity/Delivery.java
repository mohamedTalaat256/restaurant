package com.mtalaat.restaurant.modules.delivery.entity;

import com.mtalaat.restaurant.modules.delivery.enums.DeliveryStatus;
import com.mtalaat.restaurant.modules.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Core delivery record that tracks a single order from dispatch to doorstep.
 *
 * <p>Relationship summary:</p>
 * <ul>
 *   <li>Order 1 — 0..1 Delivery  (FK on Delivery side)</li>
 *   <li>Delivery 0..* — 1 Driver (current active driver)</li>
 *   <li>Delivery 1 — 0..* DeliveryAssignment (full assignment history)</li>
 *   <li>Delivery 1 — 0..* DeliveryTracking (GPS breadcrumbs)</li>
 * </ul>
 */
@Entity
@Table(name = "deliveries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The order this delivery fulfils.
     * LAZY: fetching a delivery list doesn't always require full order data.
     * No cascade: Order lifecycle is managed independently.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    /**
     * Current assigned driver (null until assignment).
     * LAZY: driver info fetched only when needed.
     * No cascade: drivers exist independently.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status = DeliveryStatus.CREATED;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "picked_up_at")
    private LocalDateTime pickedUpAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "estimated_delivery_time")
    private LocalDateTime estimatedDeliveryTime;

    @Column(name = "actual_delivery_time")
    private LocalDateTime actualDeliveryTime;

    @Column(name = "delivery_fee", precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    @Column(name = "notes", length = 500)
    private String notes;

    /**
     * Full assignment history.
     * CASCADE ALL + orphanRemoval: assignments are owned by the delivery.
     * LAZY: rarely needed outside the assignment management use-case.
     */
    @Builder.Default
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DeliveryAssignment> assignments = new ArrayList<>();

    /**
     * GPS breadcrumb trail.
     * CASCADE ALL + orphanRemoval: tracking points are owned by the delivery.
     * LAZY: only loaded when client requests live tracking.
     */
    @Builder.Default
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DeliveryTracking> trackingPoints = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
