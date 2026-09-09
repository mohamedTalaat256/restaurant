package com.mtalaat.restaurant.modules.settings.controller;

import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.settings.dto.LanguageTranslationDto;
import com.mtalaat.restaurant.modules.settings.service.LanguageTranslationService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings/language-translations")
@RequiredArgsConstructor
public class LanguageTranslationController {

    private final LanguageTranslationService languageTranslationService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.language-translation-id}")
    private Long menuId;


    @GetMapping("/translation/i18n-json")
    public ResponseEntity<ApiResponse> i18nJson() {
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_all_languages", languageTranslationService.i18nJson(), status.value()));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@Valid @RequestBody LanguageTranslationDto dto) {
        permissionChecker.checkCreate(menuId);
        LanguageTranslationDto created = languageTranslationService.add(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_language_translation_added", created, status.value()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody LanguageTranslationDto dto) {
        permissionChecker.checkCreate(menuId);
        LanguageTranslationDto created = languageTranslationService.create(dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_language_translation_created", created, status.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        LanguageTranslationDto translation = languageTranslationService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_language_translation_fetched", translation, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        permissionChecker.checkRead(menuId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_language_translation_fetched", languageTranslationService.getAll(), status.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody LanguageTranslationDto dto) {
        permissionChecker.checkEdit(menuId);
        LanguageTranslationDto updated = languageTranslationService.update(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_language_translation_updated", updated, status.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        languageTranslationService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_language_translation_deleted", null, status.value()));
    }
}
