package com.mtalaat.restaurant.modules.order.controller;

import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.order.dto.KitchenOrderActionDto;
import com.mtalaat.restaurant.modules.order.dto.KitchenOrderDto;
import com.mtalaat.restaurant.modules.order.enums.KitchenOrderStatus;
import com.mtalaat.restaurant.modules.order.service.KitchenOrderService;
import com.mtalaat.restaurant.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/kitchen")
@RequiredArgsConstructor
public class KitchenOrderController {

    private final KitchenOrderService kitchenOrderService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.kitchen-order-id}")
    private Long menuId;

    // ─────────────────────────────────────────────
    // KITCHEN DASHBOARD
    // ─────────────────────────────────────────────

    @GetMapping("/by-kitchen/{kitchenId}")
    public ResponseEntity<ApiResponse> getByKitchen(@PathVariable Long kitchenId) {
        permissionChecker.checkRead(menuId);
        List<KitchenOrderDto> orders = kitchenOrderService.getByKitchen(kitchenId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_orders_fetched", orders, status.value()));
    }

    @GetMapping("/by-kitchen/{kitchenId}/status/{orderStatus}")
    public ResponseEntity<ApiResponse> getByKitchenAndStatus(
            @PathVariable Long kitchenId,
            @PathVariable KitchenOrderStatus orderStatus) {
        permissionChecker.checkRead(menuId);
        List<KitchenOrderDto> orders = kitchenOrderService.getByKitchenAndStatus(kitchenId, orderStatus);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_orders_fetched", orders, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        KitchenOrderDto order = kitchenOrderService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_order_fetched", order, status.value()));
    }

    // ─────────────────────────────────────────────
    // KITCHEN ORDER ACTIONS
    // ─────────────────────────────────────────────

    @PostMapping("/{id}/accept")
    public ResponseEntity<ApiResponse> accept(
            @PathVariable Long id,
            @RequestBody(required = false) KitchenOrderActionDto dto) {
        permissionChecker.checkEdit(menuId);
        KitchenOrderDto updated = kitchenOrderService.accept(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_order_accepted", updated, status.value()));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse> reject(
            @PathVariable Long id,
            @RequestBody(required = false) KitchenOrderActionDto dto) {
        permissionChecker.checkEdit(menuId);
        KitchenOrderDto updated = kitchenOrderService.reject(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_order_rejected", updated, status.value()));
    }

    @PostMapping("/{id}/prepare")
    public ResponseEntity<ApiResponse> startPreparing(@PathVariable Long id) {
        permissionChecker.checkEdit(menuId);
        KitchenOrderDto updated = kitchenOrderService.startPreparing(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_order_preparing", updated, status.value()));
    }

    @PostMapping("/{id}/ready")
    public ResponseEntity<ApiResponse> markReady(@PathVariable Long id) {
        permissionChecker.checkEdit(menuId);
        KitchenOrderDto updated = kitchenOrderService.markReady(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_order_ready", updated, status.value()));
    }

    // ─────────────────────────────────────────────
    // KITCHEN ITEM ACTIONS
    // ─────────────────────────────────────────────

    @PostMapping("/{id}/items/{itemId}/ready")
    public ResponseEntity<ApiResponse> markItemReady(
            @PathVariable Long id,
            @PathVariable Long itemId) {
        permissionChecker.checkEdit(menuId);
        KitchenOrderDto updated = kitchenOrderService.markItemReady(id, itemId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_item_ready", updated, status.value()));
    }

    @PostMapping("/{id}/items/{itemId}/served")
    public ResponseEntity<ApiResponse> markItemServed(
            @PathVariable Long id,
            @PathVariable Long itemId) {
        permissionChecker.checkEdit(menuId);
        KitchenOrderDto updated = kitchenOrderService.markItemServed(id, itemId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_kitchen_item_served", updated, status.value()));
    }
}
