package com.mtalaat.restaurant.modules.settings.mapping;

import com.mtalaat.restaurant.modules.settings.dto.LanguageTranslationDto;
import com.mtalaat.restaurant.modules.settings.entity.Language;
import com.mtalaat.restaurant.modules.settings.entity.LanguageTranslation;
import org.springframework.stereotype.Component;

@Component
public class LanguageTranslationMapper {

    public LanguageTranslationDto toDto(LanguageTranslation entity) {
        return LanguageTranslationDto.builder()
                .id(entity.getId())
                .languageCode(entity.getLanguage() != null ? entity.getLanguage().getLanguageCode() : null)
                .languageName(entity.getLanguage() != null ? entity.getLanguage().getName() : null)
                .key(entity.getKey())
                .value(entity.getValue())
                .build();
    }

    public LanguageTranslation toEntity(LanguageTranslationDto dto, Language language) {
        return LanguageTranslation.builder()
                .language(language)
                .key(dto.getKey())
                .value(dto.getValue())
                .build();
    }
}
