package com.mtalaat.restaurant.modules.account.service;

import com.mtalaat.restaurant.modules.account.dto.TrialBalanceDTO;
import com.mtalaat.restaurant.modules.account.dto.TrialBalanceLineDTO;
import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.enums.AccountType;
import com.mtalaat.restaurant.modules.account.repository.AccountRepository;
import com.mtalaat.restaurant.modules.account.repository.JournalItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrialBalanceService {

    private final JournalItemRepository journalItemRepository;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public TrialBalanceDTO generate(LocalDateTime startDate, LocalDateTime endDate) {

        // Load all transactable accounts into a map
        Map<Long, Account> accountMap = accountRepository.findByAllowTransactionTrue()
                .stream().collect(Collectors.toMap(Account::getId, a -> a));

        // Pre-period sums per account
        Map<Long, BigDecimal[]> prePeriodMap = new HashMap<>();
        for (Object[] row : journalItemRepository.sumAllAccountsBeforeDate(startDate)) {
            Long accId = (Long) row[0];
            prePeriodMap.put(accId, new BigDecimal[]{(BigDecimal) row[1], (BigDecimal) row[2]});
        }

        // In-period sums per account
        Map<Long, BigDecimal[]> periodMap = new HashMap<>();
        for (Object[] row : journalItemRepository.sumAllAccountsInPeriod(startDate, endDate)) {
            Long accId = (Long) row[0];
            periodMap.put(accId, new BigDecimal[]{(BigDecimal) row[1], (BigDecimal) row[2]});
        }

        // Merge all account IDs
        Set<Long> allAccountIds = new HashSet<>();
        allAccountIds.addAll(prePeriodMap.keySet());
        allAccountIds.addAll(periodMap.keySet());

        List<TrialBalanceLineDTO> lines = new ArrayList<>();
        BigDecimal totalOpeningDebit = BigDecimal.ZERO;
        BigDecimal totalOpeningCredit = BigDecimal.ZERO;
        BigDecimal totalPeriodDebit = BigDecimal.ZERO;
        BigDecimal totalPeriodCredit = BigDecimal.ZERO;
        BigDecimal totalClosingDebit = BigDecimal.ZERO;
        BigDecimal totalClosingCredit = BigDecimal.ZERO;

        for (Long accId : allAccountIds) {
            Account account = accountMap.get(accId);
            if (account == null) continue;

            boolean isDebitNormal = (account.getType() == AccountType.ASSET || account.getType() == AccountType.EXPENSE);

            // Opening balance
            BigDecimal[] pre = prePeriodMap.getOrDefault(accId, new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            BigDecimal openingBalance = isDebitNormal
                    ? pre[0].subtract(pre[1])
                    : pre[1].subtract(pre[0]);

            // Period movement
            BigDecimal[] period = periodMap.getOrDefault(accId, new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            BigDecimal periodDebit = period[0];
            BigDecimal periodCredit = period[1];

            // Closing balance
            BigDecimal periodModifier = isDebitNormal
                    ? periodDebit.subtract(periodCredit)
                    : periodCredit.subtract(periodDebit);
            BigDecimal closingBalance = openingBalance.add(periodModifier);

            lines.add(TrialBalanceLineDTO.builder()
                    .accountId(account.getId())
                    .accountCode(account.getCode())
                    .accountName(account.getName())
                    .accountType(account.getType())
                    .openingBalance(openingBalance)
                    .totalDebit(periodDebit)
                    .totalCredit(periodCredit)
                    .closingBalance(closingBalance)
                    .build());

            // Aggregate totals (split into debit/credit columns)
            if (openingBalance.compareTo(BigDecimal.ZERO) >= 0) {
                totalOpeningDebit = totalOpeningDebit.add(isDebitNormal ? openingBalance : BigDecimal.ZERO);
                totalOpeningCredit = totalOpeningCredit.add(isDebitNormal ? BigDecimal.ZERO : openingBalance);
            } else {
                totalOpeningCredit = totalOpeningCredit.add(isDebitNormal ? openingBalance.negate() : BigDecimal.ZERO);
                totalOpeningDebit = totalOpeningDebit.add(isDebitNormal ? BigDecimal.ZERO : openingBalance.negate());
            }

            totalPeriodDebit = totalPeriodDebit.add(periodDebit);
            totalPeriodCredit = totalPeriodCredit.add(periodCredit);

            if (closingBalance.compareTo(BigDecimal.ZERO) >= 0) {
                totalClosingDebit = totalClosingDebit.add(isDebitNormal ? closingBalance : BigDecimal.ZERO);
                totalClosingCredit = totalClosingCredit.add(isDebitNormal ? BigDecimal.ZERO : closingBalance);
            } else {
                totalClosingCredit = totalClosingCredit.add(isDebitNormal ? closingBalance.negate() : BigDecimal.ZERO);
                totalClosingDebit = totalClosingDebit.add(isDebitNormal ? BigDecimal.ZERO : closingBalance.negate());
            }
        }

        // Sort by account code
        lines.sort(Comparator.comparing(TrialBalanceLineDTO::getAccountCode));

        return TrialBalanceDTO.builder()
                .fromDate(startDate)
                .toDate(endDate)
                .lines(lines)
                .totalOpeningDebit(totalOpeningDebit)
                .totalOpeningCredit(totalOpeningCredit)
                .totalPeriodDebit(totalPeriodDebit)
                .totalPeriodCredit(totalPeriodCredit)
                .totalClosingDebit(totalClosingDebit)
                .totalClosingCredit(totalClosingCredit)
                .build();
    }
}
