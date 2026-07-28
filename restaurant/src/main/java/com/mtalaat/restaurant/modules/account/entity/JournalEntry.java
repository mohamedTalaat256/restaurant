package com.mtalaat.restaurant.modules.account.entity;

import com.mtalaat.restaurant.modules.account.enums.JournalEntrySource;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "acc_journal_entries")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String entryNumber; // مثال: JV-2026-0001

    @Column(nullable = false)
    private LocalDateTime entryDate;

    @Column(nullable = false)
    private String description;

    @Column(length = 50)
    private String reference; // رقم الفاتورة أو مرجع الحركة

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private JournalEntrySource source = JournalEntrySource.SYSTEM;

    @Builder.Default
    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalItem> items = new ArrayList<>();

    public void addItem(JournalItem item) {
        items.add(item);
        item.setJournalEntry(this);
    }
}