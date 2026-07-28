package com.mtalaat.restaurant.modules.account.controller;

import com.mtalaat.restaurant.modules.account.dto.*;
import com.mtalaat.restaurant.modules.account.service.*;
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
@RequestMapping("/api/financial-reports")
@RequiredArgsConstructor
public class FinancialReportController {

    private final GeneralLedgerReportService generalLedgerReportService;
    private final TrialBalanceService trialBalanceService;
    private final ProfitLossService profitLossService;
    private final BalanceSheetService balanceSheetService;
    private final YearEndClosingService yearEndClosingService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.accounts.id}")
    private Long menuId;

    // ===================================================================
    // GENERAL LEDGER REPORT
    // ===================================================================
    @GetMapping("/general-ledger/{accountId}")
    public ResponseEntity<ApiResponse> getGeneralLedger(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        permissionChecker.checkCreate(menuId);
        GeneralLedgerReportDTO report = generalLedgerReportService.generateReport(accountId, startDate, endDate);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_gl_report_generated", report, status.value()));
    }

    // ===================================================================
    // TRIAL BALANCE
    // ===================================================================
    @GetMapping("/trial-balance")
    public ResponseEntity<ApiResponse> getTrialBalance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        permissionChecker.checkCreate(menuId);
        TrialBalanceDTO report = trialBalanceService.generate(startDate, endDate);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_trial_balance_generated", report, status.value()));
    }

    // ===================================================================
    // PROFIT & LOSS STATEMENT
    // ===================================================================
    @GetMapping("/profit-loss")
    public ResponseEntity<ApiResponse> getProfitAndLoss(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        permissionChecker.checkCreate(menuId);
        ProfitLossDTO report = profitLossService.generate(startDate, endDate);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_profit_loss_generated", report, status.value()));
    }

    // ===================================================================
    // BALANCE SHEET
    // ===================================================================
    @GetMapping("/balance-sheet")
    public ResponseEntity<ApiResponse> getBalanceSheet(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime asOfDate) {

        permissionChecker.checkCreate(menuId);
        if (asOfDate == null) {
            asOfDate = LocalDateTime.now();
        }
        BalanceSheetDTO report = balanceSheetService.generate(asOfDate);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_balance_sheet_generated", report, status.value()));
    }

    // ===================================================================
    // YEAR-END CLOSING
    // ===================================================================
    @PostMapping("/year-end-closing/{fiscalYear}")
    public ResponseEntity<ApiResponse> performYearEndClosing(@PathVariable Integer fiscalYear) {
        permissionChecker.checkCreate(menuId);
        YearEndClosingResultDTO result = yearEndClosingService.performYearEndClosing(fiscalYear);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_year_end_closing_completed", result, status.value()));
    }
}
