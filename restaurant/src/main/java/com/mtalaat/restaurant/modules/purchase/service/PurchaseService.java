package com.mtalaat.restaurant.modules.purchase.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.service.AccountParentCodes;
import com.mtalaat.restaurant.modules.account.service.AccountService;
import com.mtalaat.restaurant.modules.account.service.FinancialPostingService;
import com.mtalaat.restaurant.modules.purchase.dto.PurchaseDto;
import com.mtalaat.restaurant.modules.purchase.entity.Purchase;
import com.mtalaat.restaurant.modules.purchase.entity.PurchaseItem;
import com.mtalaat.restaurant.modules.purchase.entity.Supplier;
import com.mtalaat.restaurant.modules.purchase.mapping.PurchaseItemMapper;
import com.mtalaat.restaurant.modules.purchase.mapping.PurchaseMapper;
import com.mtalaat.restaurant.modules.purchase.repository.PurchaseRepository;
import com.mtalaat.restaurant.modules.purchase.repository.SupplierRepository;
import com.mtalaat.restaurant.modules.settings.entity.PaymentMethod;
import com.mtalaat.restaurant.modules.settings.service.LanguageTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseMapper purchaseMapper;
    private final PurchaseItemMapper purchaseItemMapper;
    private final AccountParentCodes accountParentCodes;
    private final FinancialPostingService financialPostingService;
    private final AccountService accountService;

    private final LanguageTranslationService languageTranslationService;

    @Transactional
    public PurchaseDto create(PurchaseDto dto) {

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + dto.getSupplierId()));
        Purchase entity = purchaseMapper.toEntity(dto, dto.getPaymentMethod(), supplier);

        //make money operation
        BigDecimal totalAmount = BigDecimal.valueOf(calculateTotalAmount(entity.getItems()));

        Account cashSubAccount =  accountService.findAccountByCode(accountParentCodes.getCashSubAccount());
        Account inventorySubAccount = accountService.findAccountByCode(accountParentCodes.getInventorySubAccount());
        if ( dto.getPaymentMethod() == PaymentMethod.CASH) {

            financialPostingService.postSupplierPurchase(
                    inventorySubAccount.getId(),
                    cashSubAccount.getId(),
                    totalAmount,
                    dto.getInvoiceNumber(),
                    Boolean.TRUE
            );
        }else{
            BigDecimal paidAmount = new BigDecimal(dto.getPaidAmount());

            // 1. أثبت الفاتورة بالكامل (totalAmount = 50) على حساب المورد كحركة آجلة
            // المخزن سيزيد بـ 50 (Asset -> Debit) والمورد سيزيد بـ 50 (Liability -> Credit)
            financialPostingService.postSupplierPurchase(
                    inventorySubAccount.getId(),
                    supplier.getAccount().getId(),
                    totalAmount, // القيمة الكاملة للفاتورة (50)
                    dto.getInvoiceNumber(),
                    Boolean.FALSE         // false لأنها تثبت في حساب المورد أولاً
            );

            // 2. إذا كان هناك مبلغ مدفوع (paidAmount = 30) قم بعمل قيد السداد فوراً
            // المورد يقل بـ 30 (Debit) والخزنة تقل بـ 30 (Credit)
            if (dto.getPaidAmount().compareTo(0D) > 0) {
                financialPostingService.postSupplierPayment(
                        supplier.getAccount().getId(),
                        cashSubAccount.getId(), // ID الخزنة الرئيسية
                        paidAmount,   // المبلغ المدفوع (30)
                        dto.getInvoiceNumber()  // مرجع الفاتورة
                );
            }
        }

        return purchaseMapper.toDto(purchaseRepository.save(entity));
    }

    public PurchaseDto getById(Long id) {
        Purchase entity = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + id));
        return purchaseMapper.toDto(entity);
    }

    public List<PurchaseDto> getAll() {
        return purchaseRepository.findAll().stream().map(purchaseMapper::toDto).toList();
    }

    public PurchaseDto update(Long id, PurchaseDto dto) {
        Purchase entity = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + id));
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + dto.getSupplierId()));

        try{


            List<PurchaseItem> newItems = dto.getPurchaseItems()
                    .stream()
                    .map(purchaseItemMapper::toEntity)
                    .toList();

            entity.getItems().clear();
            entity.getItems().addAll(newItems);

            entity.setInvoiceNumber(dto.getInvoiceNumber());
            entity.setPaymentMethod(dto.getPaymentMethod());
            entity.setSupplier(supplier);
            entity.setPurchaseDate(dto.getPurchaseDate());
            entity.setExpiryDate(dto.getExpiryDate());
            entity.setTotalAmount(calculateTotalAmount(newItems));
            entity.setPaidAmount(dto.getPaidAmount());
            entity.setNote(dto.getNote());

            return purchaseMapper.toDto(purchaseRepository.save(entity));
        }catch(Exception e){
          e.printStackTrace();
        }
       return null;
    }

    public void delete(Long id) {
        Purchase entity = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + id));
        purchaseRepository.delete(entity);
    }

    private Double calculateTotalAmount(List<PurchaseItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
    }
}
