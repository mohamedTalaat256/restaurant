package com.mtalaat.restaurant.modules.account.controller;


import com.mtalaat.restaurant.modules.account.dto.SupplierStatementReport;
import com.mtalaat.restaurant.modules.account.enums.AccountType;
import com.mtalaat.restaurant.modules.account.service.AccountService;
import com.mtalaat.restaurant.modules.account.service.SupplierReportService;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final PermissionChecker permissionChecker;
    private final SupplierReportService supplierReportService;

    @Value("${app.menu.accounts.id}")
    private Long menuId;

    // جلب القائمة مسطحة (لأغراض الـ Dropdowns والسليكت)
    @GetMapping("/flat")
    public ResponseEntity<ApiResponse> getAllAccountsFlat() {
        permissionChecker.checkCreate(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_accounts_fetched", accountService.getAllAccountsFlat(), status.value()));
    }

    // جلب القائمة على شكل شجرة متداخلة (لشاشة شجرة الحسابات الرئيسية)
    @GetMapping("/tree")
    public ResponseEntity<ApiResponse> getAllAccountsAsTree() {
        permissionChecker.checkCreate(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_accounts_fetched", accountService.getAllAccountsAsTree(), status.value()));
    }

    @GetMapping("types")
    public ResponseEntity<ApiResponse> getAllAccountTypes() {
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status).body(ApiResponse.success("msg_accounts_fetched", AccountType.values()  , status.value()));
    }


    @GetMapping("/{supplierAccountId}/statement")
    public ResponseEntity<ApiResponse> getSupplierStatement(
            @PathVariable Long supplierAccountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

        permissionChecker.checkCreate(menuId);

        // إذا لم يتم تحديد التواريخ، نقوم بتعيين الشهر الحالي تلقائياً كمدى افتراضي
        if (fromDate == null) {
            fromDate = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN); // بداية الشهر الحالي
        }
        if (toDate == null) {
            toDate = LocalDateTime.now().withDayOfMonth(29).with(LocalTime.MAX); // نهاية اليوم الحالي
        }

        SupplierStatementReport report = supplierReportService.generateSupplierReport(supplierAccountId, fromDate, toDate);

        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status).body(ApiResponse.success("msg_accounts_fetched", report , status.value()));
    }
}
