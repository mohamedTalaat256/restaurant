package com.mtalaat.restaurant.modules.foodManagement.controller;

import com.mtalaat.restaurant.payload.ApiResponse;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.foodManagement.dto.ItemCategoryDto;
import com.mtalaat.restaurant.modules.foodManagement.service.ItemCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/food-management/item-categories")
@RequiredArgsConstructor
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.item-category-id}")
    private Long menuId;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@Valid @ModelAttribute ItemCategoryDto dto,
                                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        permissionChecker.checkCreate(menuId);
        ItemCategoryDto created = itemCategoryService.add(dto, imageFile);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_category_added", created, status.value()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @ModelAttribute ItemCategoryDto dto,
                                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        permissionChecker.checkCreate(menuId);
        ItemCategoryDto created = itemCategoryService.create(dto, imageFile);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_category_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        ItemCategoryDto itemCategory = itemCategoryService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_category_fetched", itemCategory, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_category_fetched", itemCategoryService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @ModelAttribute ItemCategoryDto dto,
                                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        permissionChecker.checkEdit(menuId);
        ItemCategoryDto updated = itemCategoryService.update(id, dto, imageFile);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_category_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        itemCategoryService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_category_deleted", null, status.value()));
    }
}
