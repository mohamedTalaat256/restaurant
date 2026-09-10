package com.mtalaat.restaurant.modules.order.controller;

import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.account.dto.JournalEntryDTO;
import com.mtalaat.restaurant.modules.order.dto.*;
import com.mtalaat.restaurant.modules.order.enums.OrderStatus;
import com.mtalaat.restaurant.modules.order.service.OrderService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.order-id}")
    private Long menuId;

    // ─────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@Valid @RequestBody CreateOrderRequestDto dto) {
        permissionChecker.checkCreate(menuId);
        User currentUser = permissionChecker.getCurrentUser();
        OrderDto created = orderService.createOrder(dto, currentUser);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_created", created, status.value()));
    }

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        OrderDto order = orderService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_fetched", order, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        permissionChecker.checkRead(menuId);

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<OrderDto> ordersPage = orderStatus != null
                ? orderService.getByStatus(pageable, orderStatus)
                : orderService.getAll(pageable);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_orders_fetched", ordersPage, status.value()));
    }

    // ─────────────────────────────────────────────
    // UPDATE ITEMS
    // ─────────────────────────────────────────────

    @PostMapping("/{id}/items")
    public ResponseEntity<ApiResponse> addItem(
            @PathVariable Long id,
            @Valid @RequestBody OrderItemRequestDto dto) {
        permissionChecker.checkEdit(menuId);
        OrderDto updated = orderService.addItem(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_item_added", updated, status.value()));
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<ApiResponse> removeItem(
            @PathVariable Long id,
            @PathVariable Long itemId) {
        permissionChecker.checkEdit(menuId);
        OrderDto updated = orderService.removeItem(id, itemId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_item_removed", updated, status.value()));
    }

    @PatchMapping("/{id}/items/{itemId}/quantity")
    public ResponseEntity<ApiResponse> updateItemQuantity(
            @PathVariable Long id,
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        permissionChecker.checkEdit(menuId);
        OrderDto updated = orderService.updateItemQuantity(id, itemId, quantity);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_item_updated", updated, status.value()));
    }

    // ─────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        orderService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_deleted", null, status.value()));
    }

    // ─────────────────────────────────────────────
    // MERGE / SPLIT
    // ─────────────────────────────────────────────

    @PostMapping("/merge")
    public ResponseEntity<ApiResponse> mergeOrders(@Valid @RequestBody MergeOrdersDto dto) {
        permissionChecker.checkEdit(menuId);
        OrderDto merged = orderService.mergeOrders(dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_orders_merged", merged, status.value()));
    }

    @PostMapping("/{id}/split")
    public ResponseEntity<ApiResponse> splitOrder(
            @PathVariable Long id,
            @Valid @RequestBody SplitOrderDto dto) {
        permissionChecker.checkEdit(menuId);
        List<OrderDto> splitOrders = orderService.splitOrder(id, dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_split", splitOrders, status.value()));
    }

    // ─────────────────────────────────────────────
    // COMPLETE & CHECKOUT
    // ─────────────────────────────────────────────

    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse> completeOrder(@PathVariable Long id) {
        permissionChecker.checkEdit(menuId);
        OrderDto completed = orderService.completeOrder(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_completed", completed, status.value()));
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<ApiResponse> checkout(
            @PathVariable Long id,
            @Valid @RequestBody CheckoutDto dto) {
        permissionChecker.checkEdit(menuId);
        JournalEntryDTO journalEntry = orderService.checkout(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_checked_out", journalEntry, status.value()));
    }

    // ─────────────────────────────────────────────
    // TRACKING
    // ─────────────────────────────────────────────

    @GetMapping("/{id}/tracking")
    public ResponseEntity<ApiResponse> tracking(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        OrderTrackingDto tracking = orderService.getTracking(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_tracking_fetched", tracking, status.value()));
    }
}
