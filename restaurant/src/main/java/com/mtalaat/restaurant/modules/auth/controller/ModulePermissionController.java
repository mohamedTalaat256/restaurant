package com.mtalaat.restaurant.modules.auth.controller;

import com.mtalaat.restaurant.modules.auth.dto.ModulePermissionDto;
import com.mtalaat.restaurant.payload.ApiResponse;
import com.mtalaat.restaurant.modules.auth.service.ModulePermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/module-permissions")
@RequiredArgsConstructor
public class ModulePermissionController {

    private final ModulePermissionService modulePermissionService;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ModulePermissionDto dto) {
        ModulePermissionDto created = modulePermissionService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("Module permission created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ModulePermissionDto modulePermission = modulePermissionService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("Module permission fetched", modulePermission, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("Module permissions fetched", modulePermissionService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody ModulePermissionDto dto) {
        ModulePermissionDto updated = modulePermissionService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("Module permission updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        modulePermissionService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("Module permission deleted", null, status.value()));
    }
}
