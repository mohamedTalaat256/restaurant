package com.mtalaat.restaurant.modules.order.entity;

import com.mtalaat.restaurant.modules.order.enums.KitchenOrderItemStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "kitchen_order_items")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KitchenOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kitchen_order_id", nullable = false)
    private KitchenOrder kitchenOrder;

    /**
     * References the original order item to preserve all item details (name, price, variant, add-ons).
     * This is the preferred design over duplicating fields, since kitchen items are always
     * tied to a specific order item and do not need independent lifecycle management.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private KitchenOrderItemStatus status = KitchenOrderItemStatus.PENDING;

    @Column(name = "notes")
    private String notes;
}
