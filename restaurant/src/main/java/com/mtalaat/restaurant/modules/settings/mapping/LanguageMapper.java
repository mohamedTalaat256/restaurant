package com.mtalaat.restaurant.modules.settings.mapping;

import com.mtalaat.restaurant.modules.settings.dto.LanguageDto;
import com.mtalaat.restaurant.modules.settings.entity.Language;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper {

    public LanguageDto toDto(Language entity) {
        return LanguageDto.builder()
                .languageCode(entity.getLanguageCode())
                .name(entity.getName())
                .isDefault(entity.getIsDefault())
                .build();
    }

    public Language toEntity(LanguageDto dto) {
        return Language.builder()
                .languageCode(dto.getLanguageCode())
                .name(dto.getName())
                .isDefault(dto.getIsDefault() == null ? Boolean.FALSE : dto.getIsDefault())
                .build();
    }
}
