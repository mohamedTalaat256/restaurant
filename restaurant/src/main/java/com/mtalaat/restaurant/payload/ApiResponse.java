package com.mtalaat.restaurant.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private boolean status;
    private String message;
    private Object data;
    private int code;

    public static ApiResponse success(String message, Object data, int code) {
        return ApiResponse.builder()
                .status(true)
                .message(message)
                .data(data)
                .code(code)
                .build();
    }

    public static ApiResponse error(String message, Object data, int code) {
        return ApiResponse.builder()
                .status(false)
                .message(message)
                .data(data)
                .code(code)
                .build();
    }
}
