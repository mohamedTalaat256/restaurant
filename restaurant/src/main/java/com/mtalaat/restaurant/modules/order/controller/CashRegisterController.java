package com.mtalaat.restaurant.modules.order.controller;

import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.order.dto.CashRegisterDto;
import com.mtalaat.restaurant.modules.order.dto.CloseCashRegisterDto;
import com.mtalaat.restaurant.modules.order.dto.OpenCashRegisterDto;
import com.mtalaat.restaurant.modules.order.service.CashRegisterService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders/cash/registers")
@RequiredArgsConstructor
public class CashRegisterController {

    private final CashRegisterService cashRegisterService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.cash-register-id}")
    private Long menuId;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        CashRegisterDto cashRegister = cashRegisterService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cash_register_fetched", cashRegister, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cash_register_fetched", cashRegisterService.getAll(), status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        cashRegisterService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cash_register_deleted", null, status.value()));
    }

    @GetMapping("/my-open")
    public ResponseEntity<ApiResponse> getMyOpenCashRegister() {
        User currentUser = permissionChecker.getCurrentUser();
        Optional<CashRegisterDto> openRegister = cashRegisterService.getOpenCashRegister(currentUser);
        HttpStatus status = HttpStatus.OK;
        if (openRegister.isPresent()) {
            return ResponseEntity.status(status)
                    .body(ApiResponse.success("msg_cash_register_fetched", openRegister.get(), status.value()));
        }
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_no_open_cash_register", null, status.value()));
    }

    @PostMapping("/open")
    public ResponseEntity<ApiResponse> openCashRegister(@Valid @RequestBody OpenCashRegisterDto dto) {
        permissionChecker.checkCreate(menuId);
        User currentUser = permissionChecker.getCurrentUser();
        CashRegisterDto opened = cashRegisterService.openCashRegister(dto, currentUser);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cash_register_opened", opened, status.value()));
    }

    @PostMapping("/close")
    public ResponseEntity<ApiResponse> closeCashRegister(@Valid @RequestBody CloseCashRegisterDto dto) {
        permissionChecker.checkEdit(menuId);
        User currentUser = permissionChecker.getCurrentUser();
        CashRegisterDto closed = cashRegisterService.closeCashRegister(dto, currentUser);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cash_register_closed", closed, status.value()));
    }
}
