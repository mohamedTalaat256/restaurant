package com.mtalaat.restaurant.modules.foodManagement.controller;

import com.mtalaat.restaurant.payload.ApiResponse;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodVariantDto;
import com.mtalaat.restaurant.modules.foodManagement.service.ItemFoodVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/food-management/item-food-variants")
@RequiredArgsConstructor
public class ItemFoodVariantController {

    private final ItemFoodVariantService itemFoodVariantService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.item-food-variant-id}")
    private Long menuId;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@Valid @RequestBody ItemFoodVariantDto dto) {
        permissionChecker.checkCreate(menuId);
        ItemFoodVariantDto created = itemFoodVariantService.add(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_variant_added", created, status.value()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ItemFoodVariantDto dto) {
        permissionChecker.checkCreate(menuId);
        ItemFoodVariantDto created = itemFoodVariantService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_variant_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        ItemFoodVariantDto itemFoodVariant = itemFoodVariantService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_variant_fetched", itemFoodVariant, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_variant_fetched", itemFoodVariantService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody ItemFoodVariantDto dto) {
        permissionChecker.checkEdit(menuId);
        ItemFoodVariantDto updated = itemFoodVariantService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_variant_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        itemFoodVariantService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_variant_deleted", null, status.value()));
    }
}
