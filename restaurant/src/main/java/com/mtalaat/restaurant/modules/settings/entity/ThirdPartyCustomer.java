package com.mtalaat.restaurant.modules.settings.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "third_party_customers")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "commission_percentage", nullable = false)
    private Double commissionPercentage;
}
