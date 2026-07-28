package com.mtalaat.restaurant.modules.settings.controller;

import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.settings.dto.ThirdPartyCustomerDto;
import com.mtalaat.restaurant.modules.settings.service.ThirdPartyCustomerService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings/third-party-customers")
@RequiredArgsConstructor
public class ThirdPartyCustomerController {

    private final ThirdPartyCustomerService thirdPartyCustomerService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.third-party-customer-id}")
    private Long menuId;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@Valid @RequestBody ThirdPartyCustomerDto dto) {
        permissionChecker.checkCreate(menuId);
        ThirdPartyCustomerDto created = thirdPartyCustomerService.add(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_third_party_customer_added", created, status.value()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ThirdPartyCustomerDto dto) {
        permissionChecker.checkCreate(menuId);
        ThirdPartyCustomerDto created = thirdPartyCustomerService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_third_party_customer_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        ThirdPartyCustomerDto thirdPartyCustomer = thirdPartyCustomerService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_third_party_customer_fetched", thirdPartyCustomer, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_third_party_customer_fetched", thirdPartyCustomerService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody ThirdPartyCustomerDto dto) {
        permissionChecker.checkEdit(menuId);
        ThirdPartyCustomerDto updated = thirdPartyCustomerService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_third_party_customer_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        thirdPartyCustomerService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_third_party_customer_deleted", null, status.value()));
    }
}
