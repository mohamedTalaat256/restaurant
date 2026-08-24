package com.mtalaat.restaurant.modules.settings.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.settings.dto.LanguageDto;
import com.mtalaat.restaurant.modules.settings.entity.Language;
import com.mtalaat.restaurant.modules.settings.mapping.LanguageMapper;
import com.mtalaat.restaurant.modules.settings.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    public LanguageDto add(LanguageDto dto) {
        return create(dto);
    }

    public LanguageDto create(LanguageDto dto) {
        Language entity = languageMapper.toEntity(dto);
        return languageMapper.toDto(languageRepository.save(entity));
    }

    public LanguageDto getById(String code) {
        Language entity = languageRepository.findByLanguageCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code: " + code));
        return languageMapper.toDto(entity);
    }

    public List<LanguageDto> getAll() {
        return languageRepository.findAll().stream().map(languageMapper::toDto).toList();
    }

    public LanguageDto update(String code, LanguageDto dto) {
        Language entity = languageRepository.findByLanguageCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code: " + code));
        entity.setName(dto.getName());
        entity.setLanguageCode(dto.getLanguageCode());
        return languageMapper.toDto(languageRepository.save(entity));
    }

    public void delete(String code) {
        Language entity = languageRepository.findByLanguageCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code: " + code));
        languageRepository.delete(entity);
    }
}
