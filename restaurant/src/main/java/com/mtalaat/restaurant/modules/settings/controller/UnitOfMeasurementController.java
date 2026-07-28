package com.mtalaat.restaurant.modules.settings.controller;

import com.mtalaat.restaurant.payload.ApiResponse;
import com.mtalaat.restaurant.modules.settings.dto.UnitOfMeasurementDto;
import com.mtalaat.restaurant.modules.settings.service.UnitOfMeasurementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings/unit-of-measurements")
@RequiredArgsConstructor
public class UnitOfMeasurementController {

    private final UnitOfMeasurementService unitOfMeasurementService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@Valid @RequestBody UnitOfMeasurementDto dto) {
        UnitOfMeasurementDto created = unitOfMeasurementService.add(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_uom_added", created, status.value()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody UnitOfMeasurementDto dto) {
        UnitOfMeasurementDto created = unitOfMeasurementService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_uom_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        UnitOfMeasurementDto unitOfMeasurement = unitOfMeasurementService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_uom_fetched", unitOfMeasurement, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_uom_fetched", unitOfMeasurementService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody UnitOfMeasurementDto dto) {
        UnitOfMeasurementDto updated = unitOfMeasurementService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_uom_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        unitOfMeasurementService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_uom_deleted", null, status.value()));
    }
}
