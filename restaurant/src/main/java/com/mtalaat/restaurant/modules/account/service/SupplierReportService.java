package com.mtalaat.restaurant.modules.account.service;

import com.mtalaat.restaurant.modules.account.dto.SupplierReportItemDto;
import com.mtalaat.restaurant.modules.account.dto.SupplierStatementReport;
import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.repository.AccountRepository;
import com.mtalaat.restaurant.modules.account.repository.JournalItemRepository;
import com.mtalaat.restaurant.modules.purchase.repository.SupplierRepository;
import com.mtalaat.restaurant.modules.settings.service.LanguageTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SupplierReportService {


    private final JournalItemRepository journalItemRepository;
    private final AccountRepository accountRepository;
    private final SupplierRepository supplierRepository;
    private final LanguageTranslationService translate;

    public SupplierStatementReport generateSupplierReport(Long supplierAccountId, LocalDateTime fromDate, LocalDateTime toDate) {
        Account account = accountRepository.findById(supplierAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // 1. حساب الرصيد الافتتاحي (يبقى كما هو)
        BigDecimal openingBalance = journalItemRepository.getOpeningBalance(supplierAccountId, fromDate);

        // 2. جلب الحركات التفصيلية من قاعدة البيانات
        List<SupplierReportItemDto> rawItems = journalItemRepository.getSupplierReportItems(supplierAccountId, fromDate, toDate);

        // 3. تجميع الحركات بناءً على الـ reference لتبسيط العرض
        Map<String, SupplierReportItemDto> groupedMap = new LinkedHashMap<>(); // LinkedHashMap للحفاظ على ترتيب التواريخ

        for (SupplierReportItemDto item : rawItems) {
            String ref = item.getReference();

            if (!groupedMap.containsKey(ref)) {
                // إذا كان المرجع يظهر لأول مرة، نضعه في الخريطة
                groupedMap.put(ref, item);
            } else {
                // إذا كان المرجع موجوداً مسبقاً (السطر الثاني للفاتورة)، نقوم بدمج القيم
                SupplierReportItemDto existingItem = groupedMap.get(ref);

                existingItem.setDebit(existingItem.getDebit().add(item.getDebit()));
                existingItem.setCredit(existingItem.getCredit().add(item.getCredit()));

                // تعديل الوصف ليكون معبراً عن الفاتورة ككل
                existingItem.setDescription( existingItem.getDescription());
            }
        }

        // 4. تحويل الخريطة المجمعة إلى قائمة واحتساب الصافي والرصيد المتحرك
        List<SupplierReportItemDto> simplifiedItems = new ArrayList<>();
        BigDecimal currentBalance = openingBalance;

        for (SupplierReportItemDto item : groupedMap.values()) {

            // حساب الصافي لكل فاتورة
            BigDecimal netDebit = item.getDebit();
            BigDecimal netCredit = item.getCredit();

            if (netCredit.compareTo(netDebit) >= 0) {
                // المشتريات أكبر من السداد -> نضع الصافي في الدائن ونصفر المدين
                item.setCredit(netCredit.subtract(netDebit));
                item.setDebit(BigDecimal.ZERO);
            } else {
                // السداد أكبر (حالات خاصة) -> نضع الصافي في المدين ونصفر الدائن
                item.setDebit(netDebit.subtract(netCredit));
                item.setCredit(BigDecimal.ZERO);
            }

            // احتساب الرصيد المتراكم بناءً على الصافي المتبقي
            currentBalance = currentBalance.add(item.getCredit()).subtract(item.getDebit());
            item.setRunningBalance(currentBalance);

            simplifiedItems.add(item);
        }

        // 5. بناء كائن التقرير النهائي المبسط
        SupplierStatementReport report = new SupplierStatementReport();
        report.setSupplierAccountId(supplierAccountId);
        report.setSupplierName(account.getName());
        report.setFromDate(fromDate);
        report.setToDate(toDate);
        report.setOpeningBalance(openingBalance);
        report.setItems(simplifiedItems); // القائمة المبسطة الجديدة
        report.setClosingBalance(currentBalance);

        return report;
    }
}
