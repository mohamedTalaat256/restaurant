package com.mtalaat.restaurant.modules.account.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "acc_journal_items")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class JournalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal debit; // مدين

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal credit; // دائن

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter;
}