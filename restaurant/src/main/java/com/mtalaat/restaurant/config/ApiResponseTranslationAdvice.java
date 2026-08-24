package com.mtalaat.restaurant.config;

import com.mtalaat.restaurant.modules.settings.service.LanguageTranslationService;
import com.mtalaat.restaurant.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
@RequiredArgsConstructor
public class ApiResponseTranslationAdvice implements ResponseBodyAdvice<Object> {

    private final LanguageTranslationService translationService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> type = returnType.getParameterType();

        // Fix: Check if it returns ApiResponse OR a ResponseEntity containing ApiResponse
        return type.isAssignableFrom(ApiResponse.class) || type.isAssignableFrom(ResponseEntity.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // This unwraps the body automatically whether it is inside a ResponseEntity or not
        if (body instanceof ApiResponse apiResponse) {
            String rawMessage = apiResponse.getMessage();
            if (rawMessage != null) {
                String translatedMessage = translationService.get(rawMessage);
                apiResponse.setMessage(translatedMessage);
            }
        }

        return body;
    }
}
