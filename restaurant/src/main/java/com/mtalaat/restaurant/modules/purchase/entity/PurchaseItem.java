package com.mtalaat.restaurant.modules.purchase.entity;

import com.mtalaat.restaurant.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "purchase_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItem extends BaseEntity {



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "price", nullable = false)
    private Double price;
}
