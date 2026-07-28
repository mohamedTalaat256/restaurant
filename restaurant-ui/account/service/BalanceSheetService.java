package com.mtalaat.restaurant.modules.account.service;

import com.mtalaat.restaurant.modules.account.dto.BalanceSheetDTO;
import com.mtalaat.restaurant.modules.account.dto.BalanceSheetSectionDTO;
import com.mtalaat.restaurant.modules.account.dto.BalanceSheetSectionDTO.BalanceSheetLineDTO;
import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.enums.AccountType;
import com.mtalaat.restaurant.modules.account.repository.AccountRepository;
import com.mtalaat.restaurant.modules.account.repository.JournalItemRepository;
import com.mtalaat.restaurant.modules.settings.service.LanguageTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalanceSheetService {

    private final JournalItemRepository journalItemRepository;
    private final AccountRepository accountRepository;
    private final LanguageTranslationService translate;

    @Transactional(readOnly = true)
    public BalanceSheetDTO generate(LocalDateTime asOfDate) {

        // Compute cumulative balances up to asOfDate from journal items
        Map<Long, BigDecimal[]> cumulativeMap = new HashMap<>();
        for (Object[] row : journalItemRepository.sumAllAccountsUpToDate(asOfDate)) {
            Long accId = (Long) row[0];
            cumulativeMap.put(accId, new BigDecimal[]{(BigDecimal) row[1], (BigDecimal) row[2]});
        }

        // Load all transactable accounts grouped by type
        Map<AccountType, List<Account>> accountsByType = accountRepository.findByAllowTransactionTrue()
                .stream().collect(Collectors.groupingBy(Account::getType));

        // Build sections
        BalanceSheetSectionDTO assetsSection = buildSection(
                translate.get("label_assets"),
                accountsByType.getOrDefault(AccountType.ASSET, List.of()),
                cumulativeMap, true);

        BalanceSheetSectionDTO liabilitiesSection = buildSection(
                translate.get("label_liabilities"),
                accountsByType.getOrDefault(AccountType.LIABILITY, List.of()),
                cumulativeMap, false);

        BalanceSheetSectionDTO equitySection = buildSection(
                translate.get("label_equity"),
                accountsByType.getOrDefault(AccountType.EQUITY, List.of()),
                cumulativeMap, false);

        BigDecimal totalLiabilitiesAndEquity = liabilitiesSection.getSectionTotal().add(equitySection.getSectionTotal());

        // Check accounting equation: Assets = Liabilities + Equity
        boolean isBalanced = assetsSection.getSectionTotal().compareTo(totalLiabilitiesAndEquity) == 0;

        return BalanceSheetDTO.builder()
                .asOfDate(asOfDate)
                .assets(assetsSection)
                .liabilities(liabilitiesSection)
                .equity(equitySection)
                .totalLiabilitiesAndEquity(totalLiabilitiesAndEquity)
                .isBalanced(isBalanced)
                .build();
    }

    private BalanceSheetSectionDTO buildSection(String sectionName, List<Account> accounts,
                                                 Map<Long, BigDecimal[]> cumulativeMap, boolean isDebitNormal) {
        List<BalanceSheetLineDTO> lines = new ArrayList<>();
        BigDecimal sectionTotal = BigDecimal.ZERO;

        for (Account acc : accounts) {
            BigDecimal[] sums = cumulativeMap.getOrDefault(acc.getId(), new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            BigDecimal balance = isDebitNormal
                    ? sums[0].subtract(sums[1])
                    : sums[1].subtract(sums[0]);

            if (balance.compareTo(BigDecimal.ZERO) == 0) continue;

            lines.add(BalanceSheetLineDTO.builder()
                    .accountId(acc.getId())
                    .accountCode(acc.getCode())
                    .accountName(acc.getName())
                    .balance(balance)
                    .build());
            sectionTotal = sectionTotal.add(balance);
        }

        lines.sort(Comparator.comparing(BalanceSheetLineDTO::getAccountCode));

        return BalanceSheetSectionDTO.builder()
                .sectionName(sectionName)
                .accounts(lines)
                .sectionTotal(sectionTotal)
                .build();
    }
}
