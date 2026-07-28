package com.mtalaat.restaurant.modules.account.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.account.dto.GeneralLedgerLineDTO;
import com.mtalaat.restaurant.modules.account.dto.GeneralLedgerReportDTO;
import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.entity.JournalItem;
import com.mtalaat.restaurant.modules.account.enums.AccountType;
import com.mtalaat.restaurant.modules.account.repository.AccountRepository;
import com.mtalaat.restaurant.modules.account.repository.JournalItemRepository;
import com.mtalaat.restaurant.modules.settings.service.LanguageTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneralLedgerReportService {

    private final JournalItemRepository journalItemRepository;
    private final AccountRepository accountRepository;
    private final LanguageTranslationService translate;

    @Transactional(readOnly = true)
    public GeneralLedgerReportDTO generateReport(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(translate.get("error_accounting_account_not_found")));

        boolean isDebitNormal = (account.getType() == AccountType.ASSET || account.getType() == AccountType.EXPENSE);

        // 1. Compute opening balance (sum of all transactions before startDate)
        Object[] preSums = journalItemRepository.sumDebitCreditBeforeDate(accountId, startDate);
        BigDecimal preDebit = (BigDecimal) preSums[0];
        BigDecimal preCredit = (BigDecimal) preSums[1];
        BigDecimal openingBalance = isDebitNormal
                ? preDebit.subtract(preCredit)
                : preCredit.subtract(preDebit);

        // 2. Fetch all transactions within the date range
        List<JournalItem> items = journalItemRepository.findByAccountAndDateRange(accountId, startDate, endDate);

        // 3. Build line items with running balance
        List<GeneralLedgerLineDTO> lines = new ArrayList<>();
        BigDecimal runningBalance = openingBalance;
        BigDecimal periodTotalDebit = BigDecimal.ZERO;
        BigDecimal periodTotalCredit = BigDecimal.ZERO;

        for (JournalItem ji : items) {
            BigDecimal modifier = isDebitNormal
                    ? ji.getDebit().subtract(ji.getCredit())
                    : ji.getCredit().subtract(ji.getDebit());
            runningBalance = runningBalance.add(modifier);

            periodTotalDebit = periodTotalDebit.add(ji.getDebit());
            periodTotalCredit = periodTotalCredit.add(ji.getCredit());

            lines.add(GeneralLedgerLineDTO.builder()
                    .date(ji.getJournalEntry().getEntryDate())
                    .entryNumber(ji.getJournalEntry().getEntryNumber())
                    .description(ji.getJournalEntry().getDescription())
                    .reference(ji.getJournalEntry().getReference())
                    .debit(ji.getDebit())
                    .credit(ji.getCredit())
                    .runningBalance(runningBalance)
                    .costCenterName(ji.getCostCenter() != null ? ji.getCostCenter().getName() : null)
                    .build());
        }

        // 4. Closing balance
        BigDecimal closingBalance = runningBalance;

        return GeneralLedgerReportDTO.builder()
                .accountId(account.getId())
                .accountCode(account.getCode())
                .accountName(account.getName())
                .accountType(account.getType().name())
                .fromDate(startDate)
                .toDate(endDate)
                .openingBalance(openingBalance)
                .totalDebit(periodTotalDebit)
                .totalCredit(periodTotalCredit)
                .closingBalance(closingBalance)
                .lines(lines)
                .build();
    }
}
