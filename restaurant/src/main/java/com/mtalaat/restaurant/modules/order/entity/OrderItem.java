package com.mtalaat.restaurant.modules.order.entity;

import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFood;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFoodVariant;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_items")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_food_id", nullable = false)
    private ItemFood itemFood;

    /**
     * Snapshot of item name at time of order to preserve history.
     */
    @Column(name = "item_food_name", nullable = false)
    private String itemFoodName;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ItemFoodVariant variant;

    @Column(name = "variant_name")
    private String variantName;

    @Builder.Default
    @Column(name = "add_ons_price", nullable = false)
    private Double addOnsPrice = 0.0;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "notes")
    private String notes;

    @Builder.Default
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemAddOn> orderItemAddOns = new ArrayList<>();
}
