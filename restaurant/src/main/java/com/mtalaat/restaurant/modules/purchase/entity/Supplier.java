package com.mtalaat.restaurant.modules.purchase.entity;

import com.mtalaat.restaurant.entity.BaseEntity;
import com.mtalaat.restaurant.modules.account.entity.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Supplier extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // إذا تم حذف المورد يتم التعامل مع حسابه المالي حسب الرغبة
    @JoinColumn(name = "account_id", nullable = false) // إجباري لكل مورد حساب في شجرة الحسابات
    private Account account;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

}
