package com.mtalaat.restaurant.modules.settings.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.settings.dto.LanguageTranslationDto;
import com.mtalaat.restaurant.modules.settings.entity.ApplicationSetting;
import com.mtalaat.restaurant.modules.settings.entity.Language;
import com.mtalaat.restaurant.modules.settings.entity.LanguageTranslation;
import com.mtalaat.restaurant.modules.settings.mapping.LanguageTranslationMapper;
import com.mtalaat.restaurant.modules.settings.repository.LanguageRepository;
import com.mtalaat.restaurant.modules.settings.repository.LanguageTranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageTranslationService {

    private final LanguageTranslationRepository languageTranslationRepository;
    private final LanguageRepository languageRepository;
    private final LanguageTranslationMapper languageTranslationMapper;
    private final ApplicationSettingService applicationSettingService;

    public LanguageTranslationDto add(LanguageTranslationDto dto) {
        return create(dto);
    }

    public LanguageTranslationDto create(LanguageTranslationDto dto) {
        Language language = languageRepository.findByLanguageCode(dto.getLanguageCode())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code: " + dto.getLanguageCode()));
        LanguageTranslation entity = languageTranslationMapper.toEntity(dto, language);
        return languageTranslationMapper.toDto(languageTranslationRepository.save(entity));
    }

    public LanguageTranslationDto getById(Long id) {
        LanguageTranslation entity = languageTranslationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LanguageTranslation not found with id: " + id));
        return languageTranslationMapper.toDto(entity);
    }

    public List<LanguageTranslationDto> getAll() {
        return languageTranslationRepository.findAllByLanguageCode(applicationSettingService.GetCurrentLanguageCode()).stream().map(languageTranslationMapper::toDto).toList();
    }

    public LanguageTranslationDto update(Long id, LanguageTranslationDto dto) {
        LanguageTranslation entity = languageTranslationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LanguageTranslation not found with id: " + id));
        Language language = languageRepository.findByLanguageCode(dto.getLanguageCode())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code: " + dto.getLanguageCode()));
        entity.setLanguage(language);
        entity.setKey(dto.getKey());
        entity.setValue(dto.getValue());
        return languageTranslationMapper.toDto(languageTranslationRepository.save(entity));
    }

    public void delete(Long id) {
        LanguageTranslation entity = languageTranslationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LanguageTranslation not found with id: " + id));
        languageTranslationRepository.delete(entity);
    }


    public String get(String key){
        String languageCode = applicationSettingService.getApplicationSettings().getLanguageCode();
        if(languageTranslationRepository.findValueByKeyAndLanguageCode(key, languageCode).isPresent()){
            return languageTranslationRepository.findValueByKeyAndLanguageCode(key, languageCode).get();
        }else{
            return key;
        }

    }
}
