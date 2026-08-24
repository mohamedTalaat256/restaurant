package com.mtalaat.restaurant.modules.purchase.service;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.service.AccountParentCodes;
import com.mtalaat.restaurant.modules.account.service.AccountService;
import com.mtalaat.restaurant.modules.account.service.FinancialPostingService;
import com.mtalaat.restaurant.modules.purchase.dto.PurchaseDto;
import com.mtalaat.restaurant.modules.purchase.entity.Ingredient;
import com.mtalaat.restaurant.modules.purchase.entity.Purchase;
import com.mtalaat.restaurant.modules.purchase.entity.PurchaseItem;
import com.mtalaat.restaurant.modules.purchase.entity.Supplier;
import com.mtalaat.restaurant.modules.purchase.entity.SupplierLedger;
import com.mtalaat.restaurant.modules.purchase.enums.PurchaseStatus;
import com.mtalaat.restaurant.modules.purchase.enums.SupplierLedgerTransactionType;
import com.mtalaat.restaurant.modules.purchase.mapping.PurchaseItemMapper;
import com.mtalaat.restaurant.modules.purchase.mapping.PurchaseMapper;
import com.mtalaat.restaurant.modules.purchase.repository.IngredientRepository;
import com.mtalaat.restaurant.modules.purchase.repository.PurchaseRepository;
import com.mtalaat.restaurant.modules.purchase.repository.SupplierLedgerRepository;
import com.mtalaat.restaurant.modules.purchase.repository.SupplierRepository;
import com.mtalaat.restaurant.modules.settings.entity.PaymentMethod;
import com.mtalaat.restaurant.modules.settings.service.LanguageTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final IngredientRepository ingredientRepository;
    private final SupplierLedgerRepository supplierLedgerRepository;
    private final PurchaseMapper purchaseMapper;
    private final PurchaseItemMapper purchaseItemMapper;
    private final AccountParentCodes accountParentCodes;
    private final FinancialPostingService financialPostingService;
    private final AccountService accountService;
    private final CashService cashService;
    private final LanguageTranslationService languageTranslationService;

    // ─────────────────────────────────────────────────────────────────────────
    // CREATE (saves as DRAFT)
    // ─────────────────────────────────────────────────────────────────────────

    @Transactional
    public PurchaseDto create(PurchaseDto dto) {

        Supplier supplier = findSupplier(dto.getSupplierId());
        validateUniqueInvoice(supplier.getId(), dto.getInvoiceNumber(), null);

        Purchase entity = purchaseMapper.toEntity(dto, dto.getPaymentMethod(), supplier);
        entity.setStatus(PurchaseStatus.DRAFT);

        return purchaseMapper.toDto(purchaseRepository.save(entity));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────────────────────────────────

    public PurchaseDto getById(Long id) {
        return purchaseMapper.toDto(findPurchase(id));
    }

    public List<PurchaseDto> getAll() {
        return purchaseRepository.findAll().stream().map(purchaseMapper::toDto).toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // APPROVE
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Approves a DRAFT purchase:
     * 1. Changes status to APPROVED.
     * 2. Increases ingredient stock quantities.
     * 3. Recalculates moving-average cost for each ingredient.
     * 4. Records a SupplierLedger entry (credit = remaining balance owed).
     * 5. Deducts paidAmount from the restaurant treasury via CashService.
     */
    @Transactional
    public PurchaseDto approvePurchase(Long purchaseId) {

        Purchase purchase = findPurchase(purchaseId);

        if (purchase.getStatus() != PurchaseStatus.DRAFT) {
            throw new BadRequestException(
                    languageTranslationService.get("label_purchase")
                            +" ["
                            + purchaseId
                            + "]"
                            + languageTranslationService.get("error_cannot_be_approved_from_status")
                            +" "
                            + languageTranslationService.get(String.valueOf(purchase.getStatus())));
        }

        // 1. Change status
        purchase.setStatus(PurchaseStatus.APPROVED);

        // 2 & 3. Update stock and moving-average cost
        for (PurchaseItem item : purchase.getItems()) {
            Ingredient ingredient = item.getIngredient();

            double oldQty  = ingredient.getStockQuantity();
            double addedQty = item.getQuantity();
            double newTotalCost = (oldQty * ingredient.getAverageCost()) + (addedQty * item.getPrice());
            double newQty  = oldQty + addedQty;

            ingredient.setStockQuantity(newQty);
            ingredient.setAverageCost(newQty > 0 ? newTotalCost / newQty : 0.0);
            ingredientRepository.save(ingredient);
        }

        purchaseRepository.save(purchase);

        // 4. Supplier ledger — credit = outstanding balance (totalAmount - paidAmount)
        double outstanding = purchase.getTotalAmount() - purchase.getPaidAmount();
        recordSupplierLedger(
                purchase.getSupplier(),
                purchase.getInvoiceNumber(),
                SupplierLedgerTransactionType.PURCHASE,
                0.0,
                outstanding
        );

        // 5. Deduct cash paid
        if (purchase.getPaidAmount() > 0) {
            cashService.deductCash(
                    BigDecimal.valueOf(purchase.getPaidAmount()),
                    purchase.getInvoiceNumber()
            );
        }

        // Legacy accounting entries (kept from original implementation)
        postAccountingEntries(purchase);

        return purchaseMapper.toDto(purchase);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // VOID (internal helper — also exposed for direct use)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Voids an APPROVED purchase (Void & Replace method):
     * 1. Changes status to VOIDED.
     * 2. Reverses stock quantities (deducts what was added).
     * 3. Reverses the SupplierLedger entry.
     * 4. Returns cash that was paid back to treasury.
     */
    @Transactional
    public PurchaseDto voidPurchase(Long purchaseId) {
        Purchase purchase = findPurchase(purchaseId);

        if (purchase.getStatus() != PurchaseStatus.APPROVED) {
            throw new BadRequestException(languageTranslationService.get("error_only_approved_purchases_can_be_voided"));
        }

        // 1. Change status
        purchase.setStatus(PurchaseStatus.VOIDED);

        // 2. Reverse stock quantities
        for (PurchaseItem item : purchase.getItems()) {
            Ingredient ingredient = item.getIngredient();

            double currentQty  = ingredient.getStockQuantity();
            double reversedQty = item.getQuantity();

            if (currentQty < reversedQty) {

                throw new BadRequestException(
                        languageTranslationService.get("error_insufficient_stock_to_reverse_ingredient")
                                + " ["
                                + ingredient.getName()
                                + "]. "
                                + languageTranslationService.get("error_current_stock")
                                + ": "
                                + currentQty
                                + ", "
                                + languageTranslationService.get("error_required_reversal")
                                + ": "
                                + reversedQty
                ); 
            }

            double newQty = currentQty - reversedQty;
            // Reverse moving-average cost
            double newTotalCost = (currentQty * ingredient.getAverageCost()) - (reversedQty * item.getPrice());
            ingredient.setStockQuantity(newQty);
            ingredient.setAverageCost(newQty > 0 ? newTotalCost / newQty : 0.0);
            ingredientRepository.save(ingredient);
        }

        purchaseRepository.save(purchase);

        // 3. Reverse supplier ledger — debit cancels the previous credit
        double outstanding = purchase.getTotalAmount() - purchase.getPaidAmount();
        recordSupplierLedger(
                purchase.getSupplier(),
                purchase.getInvoiceNumber(),
                SupplierLedgerTransactionType.VOIDED,
                outstanding,
                0.0
        );

        // 4. Return cash to treasury
        if (purchase.getPaidAmount() > 0) {
            cashService.addCash(
                    BigDecimal.valueOf(purchase.getPaidAmount()),
                    purchase.getInvoiceNumber()
            );
        }

        return purchaseMapper.toDto(purchase);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UPDATE (Void & Replace)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Updates an invoice using the accounting-safe "Void & Replace" strategy:
     * - If the old invoice was APPROVED → void it first (reverse all effects).
     * - Create a brand-new invoice with the updated data and approve it.
     * - If the old invoice was still DRAFT → simply overwrite its data.
     */
    @Transactional
    public PurchaseDto updatePurchase(Long purchaseId, PurchaseDto updatedDto) {

        Purchase existing = findPurchase(purchaseId);

        if (existing.getStatus() == PurchaseStatus.VOIDED) {
            throw new BadRequestException(languageTranslationService.get("error_voided_purchase_cannot_be_modified"));
        }

        if (existing.getStatus() == PurchaseStatus.APPROVED) {
            // Step 1: void the old approved purchase
            voidPurchase(purchaseId);

            // Step 2: create a new purchase record with the updated data
            Supplier supplier = findSupplier(updatedDto.getSupplierId());
            validateUniqueInvoice(supplier.getId(), updatedDto.getInvoiceNumber(), null);

            Purchase newPurchase = purchaseMapper.toEntity(updatedDto, updatedDto.getPaymentMethod(), supplier);
            newPurchase.setStatus(PurchaseStatus.DRAFT);
            Purchase saved = purchaseRepository.save(newPurchase);

            // Step 3: immediately approve the new purchase
            return approvePurchase(saved.getId());
        }

        // DRAFT → simple field update (no stock/ledger effects yet)
        Supplier supplier = findSupplier(updatedDto.getSupplierId());
        validateUniqueInvoice(supplier.getId(), updatedDto.getInvoiceNumber(), purchaseId);

        List<PurchaseItem> newItems = updatedDto.getPurchaseItems()
                .stream()
                .map(purchaseItemMapper::toEntity)
                .toList();

        existing.getItems().clear();
        existing.getItems().addAll(newItems);
        existing.setInvoiceNumber(updatedDto.getInvoiceNumber());
        existing.setPaymentMethod(updatedDto.getPaymentMethod());
        existing.setSupplier(supplier);
        existing.setPurchaseDate(updatedDto.getPurchaseDate());
        existing.setTotalAmount(calculateTotalAmount(newItems));
        existing.setPaidAmount(updatedDto.getPaidAmount());
        existing.setNote(updatedDto.getNote());

        return purchaseMapper.toDto(purchaseRepository.save(existing));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────────────────

    @Transactional
    public void delete(Long id) {
        Purchase entity = findPurchase(id);
        if (entity.getStatus() == PurchaseStatus.APPROVED) {
            throw new BadRequestException(languageTranslationService.get("error_approved_purchase_cannot_be_deleted"));
        }
        purchaseRepository.delete(entity);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    private Purchase findPurchase(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(languageTranslationService.get("error_purchase_not_found") + id));
    }

    private Supplier findSupplier(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(languageTranslationService.get("error_supplier_not_found") + id));
    }

    /**
     * Validates that no non-VOIDED purchase exists for the same supplier + invoice number.
     * When updating a DRAFT (excludeId != null), the current record is excluded from the check.
     */
    private void validateUniqueInvoice(Long supplierId, String invoiceNumber, Long excludeId) {
        boolean duplicate = purchaseRepository
                .existsBySupplierIdAndInvoiceNumberAndStatusNot(supplierId, invoiceNumber, PurchaseStatus.VOIDED);

        if (duplicate) {
            // If updating, the match could be the record itself — fetch to confirm
            if (excludeId != null) {
                boolean isSelf = purchaseRepository
                        .findBySupplierIdAndInvoiceNumber(supplierId, invoiceNumber)
                        .map(p -> p.getId().equals(excludeId))
                        .orElse(false);
                if (isSelf) return;
            }
            throw new BadRequestException(
                    languageTranslationService.get("error_duplicate_invoice_number") + " [" + invoiceNumber + "]");
        }
    }

    private void recordSupplierLedger(Supplier supplier,
                                      String referenceId,
                                      SupplierLedgerTransactionType type,
                                      double debit,
                                      double credit) {

        double previousBalance = supplierLedgerRepository
                .findLatestBySupplier(supplier.getId())
                .map(SupplierLedger::getRunningBalance)
                .orElse(0.0);

        double runningBalance = previousBalance + credit - debit;

        SupplierLedger entry = SupplierLedger.builder()
                .supplier(supplier)
                .transactionDate(LocalDate.now())
                .transactionType(type)
                .referenceId(referenceId)
                .debit(debit)
                .credit(credit)
                .runningBalance(runningBalance)
                .build();

        supplierLedgerRepository.save(entry);
    }

    private void postAccountingEntries(Purchase purchase) {
        BigDecimal totalAmount = BigDecimal.valueOf(purchase.getTotalAmount());
        Account cashSubAccount = accountService.findAccountByCode(accountParentCodes.getCashAndBank());
        Account inventorySubAccount = accountService.findAccountByCode(accountParentCodes.getInventory());

        if (purchase.getPaymentMethod() == PaymentMethod.CASH) {
            financialPostingService.postSupplierPurchase(
                    inventorySubAccount.getId(),
                    cashSubAccount.getId(),
                    totalAmount,
                    purchase.getInvoiceNumber(),
                    Boolean.TRUE
            );
        } else {
            financialPostingService.postSupplierPurchase(
                    inventorySubAccount.getId(),
                    purchase.getSupplier().getAccount().getId(),
                    totalAmount,
                    purchase.getInvoiceNumber(),
                    Boolean.FALSE
            );

            if (purchase.getPaidAmount() > 0) {
                financialPostingService.postSupplierPayment(
                        purchase.getSupplier().getAccount().getId(),
                        cashSubAccount.getId(),
                        BigDecimal.valueOf(purchase.getPaidAmount()),
                        purchase.getInvoiceNumber()
                );
            }
        }
    }

    private Double calculateTotalAmount(List<PurchaseItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
    }
}

