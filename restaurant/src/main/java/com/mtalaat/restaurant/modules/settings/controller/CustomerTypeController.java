package com.mtalaat.restaurant.modules.settings.controller;

import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.settings.dto.CustomerTypeDto;
import com.mtalaat.restaurant.modules.settings.service.CustomerTypeService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings/customer-types")
@RequiredArgsConstructor
public class CustomerTypeController {

    private final CustomerTypeService customerTypeService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.customer-type-id}")
    private Long menuId;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@Valid @RequestBody CustomerTypeDto dto) {
        permissionChecker.checkCreate(menuId);
        CustomerTypeDto created = customerTypeService.add(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_customer_type_added", created, status.value()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CustomerTypeDto dto) {
        permissionChecker.checkCreate(menuId);
        CustomerTypeDto created = customerTypeService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_customer_type_created", created, status.value()));
    }

    @GetMapping("/{type}")
    public ResponseEntity<ApiResponse> getById(@PathVariable String type) {
        permissionChecker.checkRead(menuId);
        CustomerTypeDto customerType = customerTypeService.getById(type);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_customer_type_fetched", customerType, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_customer_type_fetched", customerTypeService.getAll(), status.value()));
    }

    @PutMapping("/{type}")
    public ResponseEntity<ApiResponse> update(@PathVariable String type, @Valid @RequestBody CustomerTypeDto dto) {
        permissionChecker.checkEdit(menuId);
        CustomerTypeDto updated = customerTypeService.update(type, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_customer_type_updated", updated, status.value()));
    }

    @DeleteMapping("/{type}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String type) {
        permissionChecker.checkDelete(menuId);
        customerTypeService.delete(type);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_customer_type_deleted", null, status.value()));
    }
}
