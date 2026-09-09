package com.mtalaat.restaurant.modules.settings.controller;

import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.settings.dto.ApplicationSettingDto;
import com.mtalaat.restaurant.modules.settings.service.ApplicationSettingService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/settings/application-settings")
@RequiredArgsConstructor
public class ApplicationSettingController {

    private final ApplicationSettingService applicationSettingService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.application-setting-id}")
    private Long menuId;


    @GetMapping
    public ResponseEntity<ApiResponse> getApplicationSettings() {
        permissionChecker.checkRead(menuId);
        ApplicationSettingDto setting = applicationSettingService.getApplicationSettings();
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_application_setting_fetched", setting, status.value()));
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse> getApplicationSettingsPublic() {
        ApplicationSettingDto setting = applicationSettingService.getApplicationSettings();
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_application_setting_fetched", setting, status.value()));
    }


    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> update(
            @Valid @ModelAttribute  ApplicationSettingDto dto,
            @RequestParam(value = "iconFile", required = false) MultipartFile iconFile,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile) {
        permissionChecker.checkEdit(menuId);
        ApplicationSettingDto updated = applicationSettingService.update(dto, iconFile, logoFile);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_application_setting_updated", updated, status.value()));
    }

}
