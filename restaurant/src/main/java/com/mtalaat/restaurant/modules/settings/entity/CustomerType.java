package com.mtalaat.restaurant.modules.settings.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_types")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerType {

    @Id
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "ordering", nullable = false)
    private Integer ordering;

    @Builder.Default
    @Column(name = "status", nullable = false)
    private Boolean status = true;
}
