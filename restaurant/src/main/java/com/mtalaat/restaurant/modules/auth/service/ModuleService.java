package com.mtalaat.restaurant.modules.auth.service;

import com.mtalaat.restaurant.modules.auth.dto.ModuleDto;
import com.mtalaat.restaurant.modules.auth.entity.ModuleEntity;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.auth.mapping.ModuleMapper;
import com.mtalaat.restaurant.modules.auth.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;

    public ModuleDto create(ModuleDto dto) {
        ModuleEntity module = moduleMapper.toEntity(dto);
        return moduleMapper.toDto(moduleRepository.save(module));
    }

    public ModuleDto getById(Long id) {
        ModuleEntity module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));
        return moduleMapper.toDto(module);
    }

    public List<ModuleDto> getAll() {
        return moduleRepository.findAll().stream().map(moduleMapper::toDto).toList();
    }

    public ModuleDto update(Long id, ModuleDto dto) {
        ModuleEntity existing = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setIcon(dto.getImage());
        existing.setStatus(dto.getStatus());

        return moduleMapper.toDto(moduleRepository.save(existing));
    }

    public void delete(Long id) {
        ModuleEntity existing = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));
        moduleRepository.delete(existing);
    }
}
