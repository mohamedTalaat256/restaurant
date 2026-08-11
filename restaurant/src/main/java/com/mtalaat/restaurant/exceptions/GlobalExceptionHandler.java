package com.mtalaat.restaurant.exceptions;

import com.mtalaat.restaurant.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(ResourceNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ex.printStackTrace(); // Log the exception trace to the console
        return ResponseEntity.status(status)
                .body(ApiResponse.error(ex.getMessage(), null, status.value()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(BadRequestException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ex.printStackTrace(); // Log the exception trace to the console
        return ResponseEntity.status(status)
                .body(ApiResponse.error(ex.getMessage(), null, status.value()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorized(UnauthorizedException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ex.printStackTrace(); // Log the exception trace to the console
        return ResponseEntity.status(status)
                .body(ApiResponse.error(ex.getMessage(), null, status.value()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse> handleForbidden(ForbiddenException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ex.printStackTrace(); // Log the exception trace to the console
        return ResponseEntity.status(status)
                .body(ApiResponse.error(ex.getMessage(), null, status.value()));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApiResponse> handleInternal(InternalServerErrorException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ex.printStackTrace(); // Log the exception trace to the console
        return ResponseEntity.status(status)
                .body(ApiResponse.error(ex.getMessage(), null, status.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ex.printStackTrace(); // Log the exception trace to the console
        return ResponseEntity.status(status)
                .body(ApiResponse.error("Validation failed", errors, status.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneral(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ex.printStackTrace(); // Log the exception trace to the console
        return ResponseEntity.status(status)
                .body(ApiResponse.error(ex.getMessage(), null, status.value()));
    }

    @ExceptionHandler(UnbalancedJournalException.class)
    public ResponseEntity<ApiResponse> handleRuntime(UnbalancedJournalException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ex.printStackTrace(); // Log the exception trace to the console
        return ResponseEntity.status(status)
                .body(ApiResponse.error(ex.getMessage(), null, status.value()));
    }

    @ExceptionHandler(PeriodLockedException.class)
    public ResponseEntity<ApiResponse> handlePeriodLocked(PeriodLockedException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ex.printStackTrace(); // Log the exception trace to the console
        return ResponseEntity.status(status)
                .body(ApiResponse.error(ex.getMessage(), null, status.value()));
    }
}
