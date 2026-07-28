package com.mtalaat.restaurant.modules.account.controller;

import com.mtalaat.restaurant.modules.account.dto.FinancialTransactionRequestDTO;
import com.mtalaat.restaurant.modules.account.dto.JournalEntryDTO;
import com.mtalaat.restaurant.modules.account.dto.ManualJournalEntryRequest;
import com.mtalaat.restaurant.modules.account.entity.JournalEntry;
import com.mtalaat.restaurant.modules.account.service.FinancialPostingService;
import com.mtalaat.restaurant.modules.account.service.ManualJournalEntryService;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/journal-entries")
@RequiredArgsConstructor
public class JournalController {

    private final ManualJournalEntryService manualJournalEntryService;
    private final FinancialPostingService financialPostingService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.accounts.id}")
    private Long menuId;

    @PostMapping("/manual")
    public ResponseEntity<ApiResponse> createManualJournalEntry(@Valid @RequestBody ManualJournalEntryRequest request) {
        permissionChecker.checkCreate(menuId);
        JournalEntryDTO result = manualJournalEntryService.createManualEntry(request);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_journal_entry_created", result, status.value()));
    }


    @PostMapping("/post")
    public ResponseEntity<ApiResponse> postFinancialTransaction(@Valid @RequestBody FinancialTransactionRequestDTO request) {
        permissionChecker.checkCreate(menuId);
        JournalEntry result = financialPostingService.postFinancialTransaction(request);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_journal_entry_created", result, status.value()));
    }

}
