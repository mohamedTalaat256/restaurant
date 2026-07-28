package com.mtalaat.restaurant.modules.settings.controller;

import com.mtalaat.restaurant.payload.ApiResponse;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.settings.dto.KitchenDto;
import com.mtalaat.restaurant.modules.settings.service.KitchenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings/kitchens")
@RequiredArgsConstructor
public class KitchenController {

    private final KitchenService kitchenService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.kitchen-id}")
    private Long menuId;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@Valid @RequestBody KitchenDto dto) {
        permissionChecker.checkCreate(menuId);
        KitchenDto created = kitchenService.add(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_added", created, status.value()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody KitchenDto dto) {
        permissionChecker.checkCreate(menuId);
        KitchenDto created = kitchenService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        KitchenDto kitchen = kitchenService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_fetched", kitchen, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_fetched", kitchenService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody KitchenDto dto) {
        permissionChecker.checkEdit(menuId);
        KitchenDto updated = kitchenService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        kitchenService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_deleted", null, status.value()));
    }
}
