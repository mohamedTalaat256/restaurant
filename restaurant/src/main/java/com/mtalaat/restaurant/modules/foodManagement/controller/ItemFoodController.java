package com.mtalaat.restaurant.modules.foodManagement.controller;

import com.mtalaat.restaurant.payload.ApiResponse;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodDto;
import com.mtalaat.restaurant.modules.foodManagement.service.ItemFoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/food-management/item-foods")
@RequiredArgsConstructor
public class ItemFoodController {

    private final ItemFoodService itemFoodService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.item-food-id}")
    private Long menuId;


    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @ModelAttribute ItemFoodDto dto,
                                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        permissionChecker.checkCreate(menuId);
        ItemFoodDto created = itemFoodService.create(dto, imageFile);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        ItemFoodDto itemFood = itemFoodService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_fetched", itemFood, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_fetched", itemFoodService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @ModelAttribute ItemFoodDto dto,
                                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        permissionChecker.checkEdit(menuId);
        ItemFoodDto updated = itemFoodService.update(id, dto, imageFile);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        itemFoodService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_deleted", null, status.value()));
    }
}
