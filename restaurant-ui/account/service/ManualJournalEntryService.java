package com.mtalaat.restaurant.modules.account.service;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.account.dto.JournalEntryDTO;
import com.mtalaat.restaurant.modules.account.dto.ManualJournalEntryRequest;
import com.mtalaat.restaurant.modules.account.dto.ManualJournalItemRequest;
import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.entity.CostCenter;
import com.mtalaat.restaurant.modules.account.entity.JournalEntry;
import com.mtalaat.restaurant.modules.account.entity.JournalItem;
import com.mtalaat.restaurant.modules.account.enums.AccountType;
import com.mtalaat.restaurant.modules.account.enums.JournalEntrySource;
import com.mtalaat.restaurant.modules.account.mapper.JournalMapper;
import com.mtalaat.restaurant.modules.account.repository.AccountRepository;
import com.mtalaat.restaurant.modules.account.repository.CostCenterRepository;
import com.mtalaat.restaurant.modules.account.repository.JournalEntryRepository;
import com.mtalaat.restaurant.modules.settings.service.LanguageTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ManualJournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final AccountRepository accountRepository;
    private final CostCenterRepository costCenterRepository;
    private final FiscalPeriodService fiscalPeriodService;
    private final JournalMapper journalMapper;
    private final LanguageTranslationService translate;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public JournalEntryDTO createManualEntry(ManualJournalEntryRequest request) {

        // 1. Validate fiscal period is not locked
        fiscalPeriodService.assertPeriodNotLocked(request.getEntryDate());

        // 2. Validate balance: Total Debit == Total Credit
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;
        for (ManualJournalItemRequest item : request.getItems()) {
            totalDebit = totalDebit.add(item.getDebit());
            totalCredit = totalCredit.add(item.getCredit());
        }

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new BadRequestException(translate.get("error_accounting_unbalanced_entry")
                    + " (" + totalDebit + " != " + totalCredit + ")");
        }

        if (totalDebit.compareTo(BigDecimal.ZERO) == 0) {
            throw new BadRequestException(translate.get("error_accounting_zero_entry"));
        }

        // 3. Validate each item: account exists, allowTransaction = true, debit/credit not both > 0
        for (ManualJournalItemRequest item : request.getItems()) {
            if (item.getDebit().compareTo(BigDecimal.ZERO) > 0 && item.getCredit().compareTo(BigDecimal.ZERO) > 0) {
                throw new BadRequestException(translate.get("error_accounting_debit_credit_same_line"));
            }
            if (item.getDebit().compareTo(BigDecimal.ZERO) == 0 && item.getCredit().compareTo(BigDecimal.ZERO) == 0) {
                throw new BadRequestException(translate.get("error_accounting_zero_line"));
            }
        }

        // 4. Generate entry number
        String prefix = "MJ-" + request.getEntryDate().getYear() + "-";
        long count = journalEntryRepository.countByEntryNumberStartingWith(prefix) + 1;
        String entryNumber = prefix + String.format("%05d", count);

        // 5. Build journal entry
        JournalEntry entry = JournalEntry.builder()
                .entryNumber(entryNumber)
                .entryDate(request.getEntryDate())
                .description(request.getDescription())
                .reference(request.getReference())
                .source(JournalEntrySource.MANUAL)
                .items(new ArrayList<>())
                .build();

        // 6. Process each item with pessimistic locking on accounts
        for (ManualJournalItemRequest itemReq : request.getItems()) {
            Account account = accountRepository.findByIdForUpdate(itemReq.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            translate.get("error_accounting_account_not_found") + " " + itemReq.getAccountId()));

            if (Boolean.FALSE.equals(account.getAllowTransaction())) {
                throw new BadRequestException(
                        translate.get("error_accounting_parent_cannot_transact") + " " + account.getName());
            }

            CostCenter costCenter = null;
            if (itemReq.getCostCenterId() != null) {
                costCenter = costCenterRepository.findById(itemReq.getCostCenterId())
                        .orElseThrow(() -> new ResourceNotFoundException(translate.get("error_cost_center_not_found")));
            }

            JournalItem item = JournalItem.builder()
                    .account(account)
                    .debit(itemReq.getDebit())
                    .credit(itemReq.getCredit())
                    .costCenter(costCenter)
                    .build();
            entry.addItem(item);

            // Update account balance
            BigDecimal balanceModifier = (account.getType() == AccountType.ASSET || account.getType() == AccountType.EXPENSE)
                    ? itemReq.getDebit().subtract(itemReq.getCredit())
                    : itemReq.getCredit().subtract(itemReq.getDebit());

            account.setBalance(account.getBalance().add(balanceModifier));
            account.setUpdatedAt(LocalDateTime.now());
            accountRepository.save(account);
        }

        JournalEntry saved = journalEntryRepository.save(entry);
        return journalMapper.toDTO(saved);
    }
}
