package com.mtalaat.restaurant.modules.foodManagement.controller;

import com.mtalaat.restaurant.payload.ApiResponse;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodAddOnsDto;
import com.mtalaat.restaurant.modules.foodManagement.service.ItemFoodAddOnsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/food-management/item-food-add-ons")
@RequiredArgsConstructor
public class ItemFoodAddOnsController {

    private final ItemFoodAddOnsService itemFoodAddOnsService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.item-food-add-ons-id}")
    private Long menuId;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@Valid @RequestBody ItemFoodAddOnsDto dto) {
        permissionChecker.checkCreate(menuId);
        ItemFoodAddOnsDto created = itemFoodAddOnsService.add(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_add_ons_added", created, status.value()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ItemFoodAddOnsDto dto) {
        permissionChecker.checkCreate(menuId);
        ItemFoodAddOnsDto created = itemFoodAddOnsService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_add_ons_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        ItemFoodAddOnsDto itemFoodAddOns = itemFoodAddOnsService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_add_ons_fetched", itemFoodAddOns, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_add_ons_fetched", itemFoodAddOnsService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody ItemFoodAddOnsDto dto) {
        permissionChecker.checkEdit(menuId);
        ItemFoodAddOnsDto updated = itemFoodAddOnsService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_add_ons_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        itemFoodAddOnsService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_add_ons_deleted", null, status.value()));
    }
}
