package com.mtalaat.restaurant.modules.foodManagement.entity;

import com.mtalaat.restaurant.entity.BaseEntity;
import com.mtalaat.restaurant.modules.settings.entity.Kitchen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_foods")
public class ItemFood extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "descrip")
    private String descrip;

    @Column(name = "component")
    private String component;

    @Column(name = "note")
    private String note;

    @Column(name = "image")
    private String image;

    @Column(name = "position")
    private Integer position;

    @Column(name = "is_group")
    private Boolean isGroup = false;

    @Column(name = "cooked_time")
    private Integer cookedTime;

    @Column(name = "offer_is_available")
    private Boolean offerIsAvailable = false;

    @Column(name = "offer_rate")
    private Double offerRate;

    @Column(name = "offer_start_date")
    private LocalDate offerStartDate;

    @Column(name = "offer_end_date")
    private LocalDate offerEndDate;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ItemCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kitchen_id")
    private Kitchen kitchen;

    @Column(name = "is_custom_qty")
    private Boolean isCustomQty = false;

    @Column(name = "is_special")
    private Boolean isSpecial = false;

    @Column(name = "product_vat")
    private Double productVat;

    @Column(name = "tax0")
    private Double tax0;

    @Column(name = "tax1")
    private Double tax1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_type_id")
    private MenuType menuType;

    @OneToMany(mappedBy = "itemFood", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ItemFoodVariant> variants;

    @OneToMany(mappedBy = "itemFood", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ItemFoodAddOnsAssociation> addOnsAssociations;

}
