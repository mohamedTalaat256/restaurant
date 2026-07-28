package com.mtalaat.restaurant.modules.purchase.entity;

import com.mtalaat.restaurant.modules.settings.entity.UnitOfMeasurement;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredients")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id")
    private UnitOfMeasurement uom;

    @Column(name = "stock_quantity", nullable = false)
    private Double stockQuantity;

    @Column(name = "min_stock_quantity", nullable = false)
    private Double minStockQuantity;

    @Builder.Default
    @Column(name = "status", nullable = false)
    private Boolean status = true;
}
