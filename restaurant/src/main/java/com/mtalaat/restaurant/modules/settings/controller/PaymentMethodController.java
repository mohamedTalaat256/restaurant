package com.mtalaat.restaurant.modules.settings.controller;

import com.mtalaat.restaurant.modules.settings.entity.PaymentMethod;
import com.mtalaat.restaurant.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_payment_method_fetched", PaymentMethod.values(), status.value()));
    }
}
