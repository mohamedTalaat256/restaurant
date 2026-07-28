package com.mtalaat.restaurant.modules.foodManagement.entity;

import com.mtalaat.restaurant.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_food_add_ons_associations")
public class ItemFoodAddOnsAssociation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_food_id")
    private ItemFood itemFood;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_food_add_ons_id")
    private ItemFoodAddOns itemFoodAddOns;
}
