package com.mtalaat.restaurant.modules.auth.controller;

import com.mtalaat.restaurant.modules.auth.dto.RolePermissionDto;
import com.mtalaat.restaurant.payload.ApiResponse;
import com.mtalaat.restaurant.modules.auth.service.RolePermissionService;
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
@RequestMapping("/api/role-permissions")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody RolePermissionDto dto) {
        RolePermissionDto created = rolePermissionService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("Role permission created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        RolePermissionDto rolePermission = rolePermissionService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("Role permission fetched", rolePermission, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("Role permissions fetched", rolePermissionService.getAll(), status.value()));
    }

    @GetMapping("by-menu-item/{menuItemId}")
    public ResponseEntity<ApiResponse> getAllByMenuItem(@PathVariable Long menuItemId) {
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("Role permissions fetched by menu id: "+menuItemId, rolePermissionService.getByMenuItem(menuItemId), status.value()));
    }

    @GetMapping("by-authenticated-user-roles")
    public ResponseEntity<ApiResponse> getByAuthUserRoles() {
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("Role permissions fetched by role id: ", rolePermissionService.getByAuthUserRoles(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody RolePermissionDto dto) {
        RolePermissionDto updated = rolePermissionService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("Role permission updated", updated, status.value()));
    }


    /*
        update or create in case of not found
    * */

    @PutMapping("/update-or-create")
    public ResponseEntity<ApiResponse> updateOrCreate( @Valid @RequestBody RolePermissionDto dto) {
        RolePermissionDto updated = rolePermissionService.updateOrCreate(dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_permission_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        rolePermissionService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("Role permission deleted", null, status.value()));
    }
}
