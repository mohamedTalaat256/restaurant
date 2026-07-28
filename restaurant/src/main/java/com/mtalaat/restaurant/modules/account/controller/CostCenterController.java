package com.mtalaat.restaurant.modules.account.controller;

import com.mtalaat.restaurant.modules.account.dto.CostCenterDTO;
import com.mtalaat.restaurant.modules.account.service.CostCenterService;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cost-centers")
@RequiredArgsConstructor
public class CostCenterController {

    private final CostCenterService costCenterService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.accounts.id}")
    private Long menuId;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkCreate(menuId);
        List<CostCenterDTO> costCenters = costCenterService.getAll();
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cost_centers_fetched", costCenters, status.value()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse> getActive() {
        permissionChecker.checkCreate(menuId);
        List<CostCenterDTO> costCenters = costCenterService.getActive();
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cost_centers_fetched", costCenters, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkCreate(menuId);
        CostCenterDTO costCenter = costCenterService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cost_center_fetched", costCenter, status.value()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody CostCenterDTO dto) {
        permissionChecker.checkCreate(menuId);
        CostCenterDTO result = costCenterService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cost_center_created", result, status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody CostCenterDTO dto) {
        permissionChecker.checkCreate(menuId);
        CostCenterDTO result = costCenterService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cost_center_updated", result, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkCreate(menuId);
        costCenterService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cost_center_deleted", null, status.value()));
    }
}
