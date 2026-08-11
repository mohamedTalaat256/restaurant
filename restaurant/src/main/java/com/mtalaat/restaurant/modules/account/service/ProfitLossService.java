package com.mtalaat.restaurant.modules.account.service;

import com.mtalaat.restaurant.modules.account.dto.ProfitLossDTO;
import com.mtalaat.restaurant.modules.account.dto.ProfitLossDTO.ProfitLossLineDTO;
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
public class ProfitLossService {

    private final JournalItemRepository journalItemRepository;
    private final AccountRepository accountRepository;
    private final AccountParentCodes parentCodes;

    @Transactional(readOnly = true)
    public ProfitLossDTO generate(LocalDateTime startDate, LocalDateTime endDate) {

        // Get in-period movements for all accounts
        Map<Long, BigDecimal[]> periodMap = new HashMap<>();
        for (Object[] row : journalItemRepository.sumAllAccountsInPeriod(startDate, endDate)) {
            Long accId = (Long) row[0];
            periodMap.put(accId, new BigDecimal[]{(BigDecimal) row[1], (BigDecimal) row[2]});
        }

        // Load all accounts by type
        List<Account> revenueAccounts = accountRepository.findByTypeIn(List.of(AccountType.REVENUE));
        List<Account> expenseAccounts = accountRepository.findByTypeIn(List.of(AccountType.EXPENSE));

        String cogsParentCode = parentCodes.getCogs();

        // REVENUE section
        List<ProfitLossLineDTO> revenueLines = new ArrayList<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Account acc : revenueAccounts) {
            if (!Boolean.TRUE.equals(acc.getAllowTransaction())) continue;
            BigDecimal[] sums = periodMap.getOrDefault(acc.getId(), new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            // Revenue is credit-normal: amount = credits - debits
            BigDecimal amount = sums[1].subtract(sums[0]);
            if (amount.compareTo(BigDecimal.ZERO) == 0) continue;

            revenueLines.add(ProfitLossLineDTO.builder()
                    .accountId(acc.getId())
                    .accountCode(acc.getCode())
                    .accountName(acc.getName())
                    .amount(amount)
                    .build());
            totalRevenue = totalRevenue.add(amount);
        }

        // EXPENSE section - split into COGS and Operating Expenses
        List<ProfitLossLineDTO> cogsLines = new ArrayList<>();
        List<ProfitLossLineDTO> opexLines = new ArrayList<>();
        BigDecimal totalCogs = BigDecimal.ZERO;
        BigDecimal totalOpex = BigDecimal.ZERO;

        for (Account acc : expenseAccounts) {
            if (!Boolean.TRUE.equals(acc.getAllowTransaction())) continue;
            BigDecimal[] sums = periodMap.getOrDefault(acc.getId(), new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            // Expense is debit-normal: amount = debits - credits
            BigDecimal amount = sums[0].subtract(sums[1]);
            if (amount.compareTo(BigDecimal.ZERO) == 0) continue;

            ProfitLossLineDTO line = ProfitLossLineDTO.builder()
                    .accountId(acc.getId())
                    .accountCode(acc.getCode())
                    .accountName(acc.getName())
                    .amount(amount)
                    .build();

            // Determine if this is a COGS account by checking if its code starts with the COGS parent code
            if (acc.getCode().startsWith(cogsParentCode)) {
                cogsLines.add(line);
                totalCogs = totalCogs.add(amount);
            } else {
                opexLines.add(line);
                totalOpex = totalOpex.add(amount);
            }
        }

        BigDecimal grossProfit = totalRevenue.subtract(totalCogs);
        BigDecimal netProfit = grossProfit.subtract(totalOpex);

        return ProfitLossDTO.builder()
                .fromDate(startDate)
                .toDate(endDate)
                .revenueAccounts(revenueLines)
                .totalRevenue(totalRevenue)
                .cogsAccounts(cogsLines)
                .totalCogs(totalCogs)
                .grossProfit(grossProfit)
                .operatingExpenseAccounts(opexLines)
                .totalOperatingExpenses(totalOpex)
                .netProfit(netProfit)
                .build();
    }
}
