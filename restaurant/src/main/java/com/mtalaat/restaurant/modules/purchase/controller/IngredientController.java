package com.mtalaat.restaurant.modules.purchase.controller;

import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.purchase.dto.IngredientDto;
import com.mtalaat.restaurant.modules.purchase.service.IngredientService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.ingredient-id}")
    private Long menuId;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody IngredientDto dto) {
        permissionChecker.checkCreate(menuId);
        IngredientDto created = ingredientService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_ingredient_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        IngredientDto ingredient = ingredientService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_ingredient_fetched", ingredient, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_ingredient_fetched", ingredientService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody IngredientDto dto) {
        permissionChecker.checkEdit(menuId);
        IngredientDto updated = ingredientService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_ingredient_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        ingredientService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_ingredient_deleted", null, status.value()));
    }
}
