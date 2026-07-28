package com.mtalaat.restaurant.modules.account.repository;

import com.mtalaat.restaurant.modules.account.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    // لتوليد رقم قيد متسلسل تلقائي
    long countByEntryNumberStartingWith(String prefix);
}