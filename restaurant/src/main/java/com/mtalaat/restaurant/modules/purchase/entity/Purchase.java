package com.mtalaat.restaurant.modules.purchase.entity;

import com.mtalaat.restaurant.entity.BaseEntity;
import com.mtalaat.restaurant.modules.settings.entity.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Purchase extends BaseEntity {

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "paid_amount", nullable = false)
    private Double paidAmount;

    @Column(name = "note")
    private String note;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "purchase_id")
    private List<PurchaseItem> items = new ArrayList<>();




}
