package com.mtalaat.restaurant.modules.account.repository;

import com.mtalaat.restaurant.modules.account.dto.SupplierReportItemDto;
import com.mtalaat.restaurant.modules.account.entity.JournalItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JournalItemRepository extends JpaRepository<JournalItem, Long> {

     @Query("SELECT COALESCE(SUM(ji.credit), 0) - COALESCE(SUM(ji.debit), 0) FROM JournalItem ji " +
            "WHERE ji.account.id = :accountId AND ji.journalEntry.entryDate < :fromDate")
    BigDecimal getOpeningBalance(@Param("accountId") Long accountId, @Param("fromDate") LocalDateTime fromDate);


    @Query("SELECT new com.mtalaat.restaurant.modules.account.dto.SupplierReportItemDto(" +
            "ji.journalEntry.entryNumber, ji.journalEntry.entryDate, ji.journalEntry.description, " +
            "ji.journalEntry.reference, ji.debit, ji.credit) " +
            "FROM JournalItem ji " +
            "WHERE ji.account.id = :accountId " +
         //   "AND ji.journalEntry.entryDate BETWEEN :fromDate AND :toDate " +
            "ORDER BY ji.journalEntry.entryDate ASC, ji.id ASC")
    List<SupplierReportItemDto> getSupplierReportItems(@Param("accountId") Long accountId,
                                                       @Param("fromDate") LocalDateTime fromDate,
                                                       @Param("toDate") LocalDateTime toDate);

    // ===================================================================
    // GENERAL LEDGER REPORT QUERIES
    // ===================================================================

    @Query("SELECT COALESCE(SUM(ji.debit), 0), COALESCE(SUM(ji.credit), 0) " +
            "FROM JournalItem ji WHERE ji.account.id = :accountId " +
            "AND ji.journalEntry.entryDate < :startDate")
    Object[] sumDebitCreditBeforeDate(@Param("accountId") Long accountId,
                                      @Param("startDate") LocalDateTime startDate);

    @Query("SELECT ji FROM JournalItem ji " +
            "JOIN FETCH ji.journalEntry je " +
            "LEFT JOIN FETCH ji.costCenter " +
            "WHERE ji.account.id = :accountId " +
            "AND je.entryDate >= :startDate AND je.entryDate <= :endDate " +
            "ORDER BY je.entryDate ASC, ji.id ASC")
    List<JournalItem> findByAccountAndDateRange(@Param("accountId") Long accountId,
                                                 @Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    // ===================================================================
    // TRIAL BALANCE & FINANCIAL STATEMENTS QUERIES
    // ===================================================================

    @Query("SELECT ji.account.id, COALESCE(SUM(ji.debit), 0), COALESCE(SUM(ji.credit), 0) " +
            "FROM JournalItem ji WHERE ji.journalEntry.entryDate < :startDate " +
            "GROUP BY ji.account.id")
    List<Object[]> sumAllAccountsBeforeDate(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT ji.account.id, COALESCE(SUM(ji.debit), 0), COALESCE(SUM(ji.credit), 0) " +
            "FROM JournalItem ji WHERE ji.journalEntry.entryDate >= :startDate " +
            "AND ji.journalEntry.entryDate <= :endDate " +
            "GROUP BY ji.account.id")
    List<Object[]> sumAllAccountsInPeriod(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT ji.account.id, COALESCE(SUM(ji.debit), 0), COALESCE(SUM(ji.credit), 0) " +
            "FROM JournalItem ji WHERE ji.journalEntry.entryDate <= :asOfDate " +
            "GROUP BY ji.account.id")
    List<Object[]> sumAllAccountsUpToDate(@Param("asOfDate") LocalDateTime asOfDate);
}