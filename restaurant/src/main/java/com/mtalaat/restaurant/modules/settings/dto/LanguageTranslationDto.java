package com.mtalaat.restaurant.modules.settings.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageTranslationDto {

    private Long id;

    @NotNull(message = "Language code is required")
    private String languageCode;

    private String languageName;

    @NotBlank(message = "Key is required")
    private String key;

    @NotBlank(message = "Value is required")
    private String value;
}
