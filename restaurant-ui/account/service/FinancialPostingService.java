package com.mtalaat.restaurant.modules.account.service;

import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.entity.JournalEntry;
import com.mtalaat.restaurant.modules.account.entity.JournalItem;
import com.mtalaat.restaurant.modules.account.enums.AccountType;
import com.mtalaat.restaurant.modules.account.repository.AccountRepository;
import com.mtalaat.restaurant.modules.account.repository.JournalEntryRepository;
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
public class FinancialPostingService {

    private final JournalEntryRepository journalEntryRepository;
    private final AccountRepository accountRepository;
    private final AccountParentCodes parentCodes;
    private final LanguageTranslationService translate;
    private final FiscalPeriodService fiscalPeriodService;

    @Transactional
    public JournalEntry saveAndPostEntry(String description, String reference, List<JournalItemBuilder> itemBuilders) {
        LocalDateTime now = LocalDateTime.now();

        // Check fiscal period lock
        fiscalPeriodService.assertPeriodNotLocked(now);

        String prefix = "JV-" + now.getYear() + "-";
        long count = journalEntryRepository.countByEntryNumberStartingWith(prefix) + 1;
        String entryNumber = prefix + String.format("%05d", count);

        JournalEntry entry = JournalEntry.builder()
                .entryNumber(entryNumber)
                .entryDate(now)
                .description(description)
                .reference(reference)
                .items(new ArrayList<>())
                .build();

        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (JournalItemBuilder builder : itemBuilders) {
            Account account = accountRepository.findByIdForUpdate(builder.accountId)
                    .orElseThrow(() -> new RuntimeException( translate.get("error_accounting_account_not_found")+" "+ builder.accountId));

            if (Boolean.FALSE.equals(account.getAllowTransaction())) {
                throw new RuntimeException( translate.get("error_accounting_parent_cannot_transact")+" "+ account.getName());
            }

            JournalItem item = JournalItem.builder().account(account).debit(builder.debit).credit(builder.credit).build();

            if (builder.costCenterId != null) {
                // CostCenter lookup is optional; set via the builder
                item.setCostCenter(null); // Will be resolved if CostCenterRepository is injected
            }

            entry.addItem(item);

            totalDebit = totalDebit.add(builder.debit);
            totalCredit = totalCredit.add(builder.credit);

            BigDecimal balanceModifier = (account.getType() == AccountType.ASSET || account.getType() == AccountType.EXPENSE)
                    ? builder.debit.subtract(builder.credit)
                    : builder.credit.subtract(builder.debit);

            account.setBalance(account.getBalance().add(balanceModifier));
            account.setUpdatedAt(now);
            accountRepository.save(account);
        }

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new RuntimeException(translate.get("error_accounting_unbalanced_entry")+" ("+ totalDebit +" - "+ totalCredit +")");
        }

        return journalEntryRepository.save(entry);
    }

    public static class JournalItemBuilder {
        Long accountId;
        BigDecimal debit;
        BigDecimal credit;
        Long costCenterId;

        public JournalItemBuilder(Long accountId, BigDecimal debit, BigDecimal credit) {
            this(accountId, debit, credit, null);
        }

        public JournalItemBuilder(Long accountId, BigDecimal debit, BigDecimal credit, Long costCenterId) {
            this.accountId = accountId;
            this.debit = debit;
            this.credit = credit;
            this.costCenterId = costCenterId;
        }
    }

    // ===================================================================
    // العمليات التشغيلية مترجمة بالكامل ديناميكياً
    // ===================================================================

    @Transactional
    public void postCustomerSale(Long creditOrCashAccountId, Long salesAccountId, BigDecimal amount, String invoiceNo, boolean isCash) {
        List<JournalItemBuilder> items = new ArrayList<>();
        items.add(new JournalItemBuilder(creditOrCashAccountId, amount, BigDecimal.ZERO));
        items.add(new JournalItemBuilder(salesAccountId, BigDecimal.ZERO, amount));

        String msgKey = isCash ? "phrase_accounting_cash_sale" : "phrase_accounting_credit_sale";
        String description = translate.get(msgKey);

        saveAndPostEntry(description+ " " + invoiceNo, invoiceNo, items);
    }

    @Transactional
    public void postSupplierPurchase(Long inventoryAccountId, Long creditOrCashAccountId, BigDecimal amount, String billNo, boolean isCash) {
        List<JournalItemBuilder> items = new ArrayList<>();
        items.add(new JournalItemBuilder(inventoryAccountId, amount, BigDecimal.ZERO));
        items.add(new JournalItemBuilder(creditOrCashAccountId, BigDecimal.ZERO, amount));

        String msgKey = isCash ? "phrase_accounting_purchase_cash" : "phrase_accounting_purchase_credit";
        String description = translate.get(msgKey);

        saveAndPostEntry(description+ " " + billNo, billNo, items);
    }

    @Transactional
    public void postSupplierPayment(Long supplierAccountId, Long cashAccountId, BigDecimal amount, String receiptNo) {
        List<JournalItemBuilder> items = new ArrayList<>();
        items.add(new JournalItemBuilder(supplierAccountId, amount, BigDecimal.ZERO));
        items.add(new JournalItemBuilder(cashAccountId, BigDecimal.ZERO, amount));

        String description = translate.get("phrase_accounting_supplier_payment");
        saveAndPostEntry(description+ " " + receiptNo, receiptNo, items);
    }

    @Transactional
    public void postSalaryPayment(Long cashAccountId, BigDecimal totalSalaries, String month) {
        Account salaryExpenseAcc = accountRepository.findByCode(parentCodes.getSalariesExpense())
                .orElseThrow(() -> new RuntimeException(translate.get("error_accounting_missing_code") + " " + parentCodes.getSalariesExpense()));

        List<JournalItemBuilder> items = new ArrayList<>();
        items.add(new JournalItemBuilder(salaryExpenseAcc.getId(), totalSalaries, BigDecimal.ZERO));
        items.add(new JournalItemBuilder(cashAccountId, BigDecimal.ZERO, totalSalaries));

        String description = translate.get("phrase_accounting_salary_payment") + " " + month;
        saveAndPostEntry(description, "HR-" + month, items);
    }

    @Transactional
    public void postCustomerCollection(Long customerAccountId, Long cashAccountId, BigDecimal amount, String receiptNo) {
        List<JournalItemBuilder> items = new ArrayList<>();
        items.add(new JournalItemBuilder(cashAccountId, amount, BigDecimal.ZERO));
        items.add(new JournalItemBuilder(customerAccountId, BigDecimal.ZERO, amount));

        String description = translate.get("phrase_accounting_customer_collection") + " " + receiptNo;
        saveAndPostEntry(description, receiptNo, items);
    }

    @Transactional
    public void postGeneralExpenseOrAsset(Long expenseOrAssetAccountId, Long cashAccountId, BigDecimal amount, String description, String reference) {
        List<JournalItemBuilder> items = new ArrayList<>();
        items.add(new JournalItemBuilder(expenseOrAssetAccountId, amount, BigDecimal.ZERO));
        items.add(new JournalItemBuilder(cashAccountId, BigDecimal.ZERO, amount));

        saveAndPostEntry(description, reference, items);
    }
}