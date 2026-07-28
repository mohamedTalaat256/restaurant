package com.mtalaat.restaurant.modules.foodManagement.controller;

import com.mtalaat.restaurant.payload.ApiResponse;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodAddOnsAssociationDto;
import com.mtalaat.restaurant.modules.foodManagement.service.ItemFoodAddOnsAssociationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/food-management/item-food-add-ons-associations")
@RequiredArgsConstructor
public class ItemFoodAddOnsAssociationController {

    private final ItemFoodAddOnsAssociationService itemFoodAddOnsAssociationService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.item-food-add-ons-association-id}")
    private Long menuId;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@Valid @RequestBody ItemFoodAddOnsAssociationDto dto) {
        permissionChecker.checkCreate(menuId);
        ItemFoodAddOnsAssociationDto created = itemFoodAddOnsAssociationService.add(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_add_ons_association_added", created, status.value()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ItemFoodAddOnsAssociationDto dto) {
        permissionChecker.checkCreate(menuId);
        ItemFoodAddOnsAssociationDto created = itemFoodAddOnsAssociationService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_add_ons_association_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        ItemFoodAddOnsAssociationDto association = itemFoodAddOnsAssociationService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_add_ons_association_fetched", association, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_add_ons_association_fetched", itemFoodAddOnsAssociationService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody ItemFoodAddOnsAssociationDto dto) {
        permissionChecker.checkEdit(menuId);
        ItemFoodAddOnsAssociationDto updated = itemFoodAddOnsAssociationService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_add_ons_association_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        itemFoodAddOnsAssociationService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_item_food_add_ons_association_deleted", null, status.value()));
    }
}
