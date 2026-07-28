package com.mtalaat.restaurant.modules.order.entity;

import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFoodAddOns;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_item_add_ons")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemAddOn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_on_id", nullable = false)
    private ItemFoodAddOns addOn;

    /**
     * Snapshot of add-on name at time of order.
     */
    @Column(name = "add_on_name", nullable = false)
    private String addOnName;

    @Column(name = "price", nullable = false)
    private Double price;
}
