package com.mtalaat.restaurant.modules.settings.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDto {

    @NotBlank(message = "Code is required")
    private String languageCode;

    @NotBlank(message = "Name is required")
    private String name;

    private Boolean isDefault;
}
