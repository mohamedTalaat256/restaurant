package com.mtalaat.restaurant.modules.account.controller;

import com.mtalaat.restaurant.modules.account.dto.FiscalPeriodDTO;
import com.mtalaat.restaurant.modules.account.service.FiscalPeriodService;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fiscal-periods")
@RequiredArgsConstructor
public class FiscalPeriodController {

    private final FiscalPeriodService fiscalPeriodService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.accounts.id}")
    private Long menuId;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPeriods() {
        permissionChecker.checkCreate(menuId);
        List<FiscalPeriodDTO> periods = fiscalPeriodService.getAllPeriods();
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_fiscal_periods_fetched", periods, status.value()));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<ApiResponse> getPeriodsByYear(@PathVariable Integer year) {
        permissionChecker.checkCreate(menuId);
        List<FiscalPeriodDTO> periods = fiscalPeriodService.getPeriodsByYear(year);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_fiscal_periods_fetched", periods, status.value()));
    }

    @PostMapping("/lock")
    public ResponseEntity<ApiResponse> lockPeriod(@RequestParam Integer year, @RequestParam Integer month) {
        permissionChecker.checkCreate(menuId);
        FiscalPeriodDTO result = fiscalPeriodService.lockPeriod(year, month);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_period_locked", result, status.value()));
    }

    @PostMapping("/unlock")
    public ResponseEntity<ApiResponse> unlockPeriod(@RequestParam Integer year, @RequestParam Integer month) {
        permissionChecker.checkCreate(menuId);
        FiscalPeriodDTO result = fiscalPeriodService.unlockPeriod(year, month);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_period_unlocked", result, status.value()));
    }
}
