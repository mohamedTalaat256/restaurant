package com.mtalaat.restaurant.modules.foodManagement.controller;

import com.mtalaat.restaurant.payload.ApiResponse;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.foodManagement.dto.MenuTypeDto;
import com.mtalaat.restaurant.modules.foodManagement.service.MenuTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/food-management/menu-types")
@RequiredArgsConstructor
public class MenuTypeController {

    private final MenuTypeService menuTypeService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.menu-type-id}")
    private Long menuId;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@Valid @ModelAttribute MenuTypeDto dto,
                                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        permissionChecker.checkCreate(menuId);
        MenuTypeDto created = menuTypeService.add(dto, imageFile);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_menu_type_added", created, status.value()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @ModelAttribute MenuTypeDto dto,
                                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        permissionChecker.checkCreate(menuId);
        MenuTypeDto created = menuTypeService.create(dto, imageFile);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_menu_type_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        MenuTypeDto menuType = menuTypeService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_menu_type_fetched", menuType, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_menu_type_fetched", menuTypeService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @ModelAttribute MenuTypeDto dto,
                                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        permissionChecker.checkEdit(menuId);
        MenuTypeDto updated = menuTypeService.update(id, dto, imageFile);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_menu_type_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        menuTypeService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_menu_type_deleted", null, status.value()));
    }
}
