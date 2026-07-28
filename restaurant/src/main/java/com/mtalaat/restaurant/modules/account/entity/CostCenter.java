package com.mtalaat.restaurant.modules.account.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "acc_cost_centers",
        uniqueConstraints = @UniqueConstraint(columnNames = "code"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @Column(nullable = false)
    private Boolean status = true;
}
