package com.mtalaat.restaurant.modules.settings.entity;

import com.mtalaat.restaurant.modules.account.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "customers")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = true) // nullable لأن العميل الكاش لا يحتاج حساب فرعي
    private Account account;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "favorite_delivery_address")
    private String favoriteDeliveryAddress;

    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_type")
    private CustomerType customerType;

    @Builder.Default
    @Column(name = "status", nullable = false)
    private Boolean status = true;


    @Column(nullable = false)
    @Builder.Default
    private Boolean allowCredit = false;

    @Column(precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal creditLimit = BigDecimal.ZERO;
}
