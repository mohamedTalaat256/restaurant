package com.mtalaat.restaurant.modules.foodManagement.entity;

import com.mtalaat.restaurant.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "item_categories")
public class ItemCategory extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "position")
    private Integer position;

    @Column(name = "is_offer")
    private Boolean isOffer = false;

    @Column(name = "offer_start_date")
    private LocalDate offerStartDate;

    @Column(name = "offer_end_date")
    private LocalDate offerEndDate;

    @Column(name = "status", nullable = false)
    private Boolean status = true;
}
