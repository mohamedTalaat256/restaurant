package com.mtalaat.restaurant.modules.account.service;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.account.dto.YearEndClosingResultDTO;
import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.entity.JournalEntry;
import com.mtalaat.restaurant.modules.account.entity.JournalItem;
import com.mtalaat.restaurant.modules.account.enums.AccountType;
import com.mtalaat.restaurant.modules.account.enums.JournalEntrySource;
import com.mtalaat.restaurant.modules.account.repository.AccountRepository;
import com.mtalaat.restaurant.modules.account.repository.JournalEntryRepository;
import com.mtalaat.restaurant.modules.settings.service.LanguageTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YearEndClosingService {

    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final AccountParentCodes parentCodes;
    private final FiscalPeriodService fiscalPeriodService;
    private final LanguageTranslationService translate;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public YearEndClosingResultDTO performYearEndClosing(Integer fiscalYear) {

        // 1. Verify all 12 months of the fiscal year are locked
        for (int month = 1; month <= 12; month++) {
            if (!fiscalPeriodService.isPeriodLocked(LocalDateTime.of(fiscalYear, month, 1, 0, 0))) {
                throw new BadRequestException(
                        translate.get("error_year_end_periods_not_locked") + " " + fiscalYear + "-" + String.format("%02d", month));
            }
        }

        // 2. Find the Retained Earnings account
        Account retainedEarnings = accountRepository.findByCode(parentCodes.getRetainedEarnings())
                .orElseThrow(() -> new ResourceNotFoundException(
                        translate.get("error_accounting_missing_code") + " " + parentCodes.getRetainedEarnings()));

        // 3. Find all Revenue and Expense accounts with non-zero balances
        List<Account> incomeExpenseAccounts = accountRepository.findByTypeInAndBalanceNotZero(
                List.of(AccountType.REVENUE, AccountType.EXPENSE));

        if (incomeExpenseAccounts.isEmpty()) {
            throw new BadRequestException(translate.get("error_year_end_no_accounts"));
        }

        // 4. Generate closing entry number
        String prefix = "YEC-" + fiscalYear + "-";
        long count = journalEntryRepository.countByEntryNumberStartingWith(prefix) + 1;
        String entryNumber = prefix + String.format("%03d", count);

        // 5. Build the closing journal entry
        LocalDateTime closingDate = LocalDateTime.of(fiscalYear, 12, 31, 23, 59, 59);

        JournalEntry closingEntry = JournalEntry.builder()
                .entryNumber(entryNumber)
                .entryDate(closingDate)
                .description(translate.get("phrase_year_end_closing") + " " + fiscalYear)
                .reference("YEC-" + fiscalYear)
                .source(JournalEntrySource.SYSTEM)
                .items(new ArrayList<>())
                .build();

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;
        int accountsClosed = 0;

        for (Account account : incomeExpenseAccounts) {
            // Lock the account row for update
            Account lockedAccount = accountRepository.findByIdForUpdate(account.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(translate.get("error_accounting_account_not_found")));

            BigDecimal balance = lockedAccount.getBalance();
            if (balance.compareTo(BigDecimal.ZERO) == 0) continue;

            JournalItem closingItem;

            if (lockedAccount.getType() == AccountType.REVENUE) {
                // Revenue (credit-normal): to zero out, debit by its balance
                closingItem = JournalItem.builder()
                        .account(lockedAccount)
                        .debit(balance)
                        .credit(BigDecimal.ZERO)
                        .build();
                totalRevenue = totalRevenue.add(balance);
            } else {
                // Expense (debit-normal): to zero out, credit by its balance
                closingItem = JournalItem.builder()
                        .account(lockedAccount)
                        .debit(BigDecimal.ZERO)
                        .credit(balance)
                        .build();
                totalExpenses = totalExpenses.add(balance);
            }

            closingEntry.addItem(closingItem);

            // Zero the account balance
            lockedAccount.setBalance(BigDecimal.ZERO);
            lockedAccount.setUpdatedAt(LocalDateTime.now());
            accountRepository.save(lockedAccount);
            accountsClosed++;
        }

        // 6. Calculate net income and post to Retained Earnings
        BigDecimal netIncome = totalRevenue.subtract(totalExpenses);

        Account retainedEarningsLocked = accountRepository.findByIdForUpdate(retainedEarnings.getId())
                .orElseThrow(() -> new ResourceNotFoundException(translate.get("error_accounting_account_not_found")));

        JournalItem retainedEarningsItem;
        if (netIncome.compareTo(BigDecimal.ZERO) >= 0) {
            // Profit: credit Retained Earnings
            retainedEarningsItem = JournalItem.builder()
                    .account(retainedEarningsLocked)
                    .debit(BigDecimal.ZERO)
                    .credit(netIncome)
                    .build();
        } else {
            // Loss: debit Retained Earnings
            retainedEarningsItem = JournalItem.builder()
                    .account(retainedEarningsLocked)
                    .debit(netIncome.negate())
                    .credit(BigDecimal.ZERO)
                    .build();
        }

        closingEntry.addItem(retainedEarningsItem);

        // Update Retained Earnings balance (equity is credit-normal)
        retainedEarningsLocked.setBalance(retainedEarningsLocked.getBalance().add(netIncome));
        retainedEarningsLocked.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(retainedEarningsLocked);

        // 7. Save the closing entry
        JournalEntry savedEntry = journalEntryRepository.save(closingEntry);

        return YearEndClosingResultDTO.builder()
                .fiscalYear(fiscalYear)
                .closingDate(closingDate)
                .closingEntryNumber(savedEntry.getEntryNumber())
                .totalRevenue(totalRevenue)
                .totalExpenses(totalExpenses)
                .netIncome(netIncome)
                .accountsClosed(accountsClosed)
                .build();
    }
}
