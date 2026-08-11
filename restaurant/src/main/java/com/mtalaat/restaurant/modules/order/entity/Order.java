package com.mtalaat.restaurant.modules.order.entity;

import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.delivery.entity.Delivery;
import com.mtalaat.restaurant.modules.order.enums.OrderStatus;
import com.mtalaat.restaurant.modules.order.enums.OrderType;
import com.mtalaat.restaurant.modules.settings.entity.Customer;
import com.mtalaat.restaurant.modules.settings.entity.RestaurantTable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "customer_type")
    private String customerType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "third_party_customer_id")
    private Customer thirdPartyCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private RestaurantTable table;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiter_id")
    private User waiter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_register_id")
    private CashRegister cashRegister;

    @Builder.Default
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount = 0.0;

    @Column(name = "notes")
    private String notes;

    /**
     * For split orders: references the original order from which this was split.
     */
    @Column(name = "parent_order_id")
    private Long parentOrderId;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KitchenOrder> kitchenOrders = new ArrayList<>();

    /**
     * The delivery record for this order, if it is of type DELIVERY.
     * mappedBy: FK lives on the Delivery side.
     * LAZY: delivery data is only needed in delivery-specific use-cases.
     * No cascade: Delivery lifecycle is managed independently by DeliveryService.
     */
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Delivery delivery;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
