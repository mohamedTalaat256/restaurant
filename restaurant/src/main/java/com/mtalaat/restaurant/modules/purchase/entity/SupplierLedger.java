package com.mtalaat.restaurant.modules.purchase.entity;

import com.mtalaat.restaurant.modules.purchase.enums.SupplierLedgerTransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "supplier_ledger")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private SupplierLedgerTransactionType transactionType;

    /** رقم الفاتورة أو أي مرجع خارجي */
    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    /** المبلغ المستحق على المطعم (دين على المطعم) */
    @Column(name = "debit", nullable = false)
    private Double debit;

    /** المبلغ المدفوع / المُضاف للمورد */
    @Column(name = "credit", nullable = false)
    private Double credit;

    /** الرصيد التراكمي بعد هذه الحركة */
    @Column(name = "running_balance", nullable = false)
    private Double runningBalance;
}
