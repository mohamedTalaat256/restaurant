package com.mtalaat.restaurant.modules.auth.service;

import com.mtalaat.restaurant.modules.auth.dto.ModulePermissionDto;
import com.mtalaat.restaurant.modules.auth.entity.ModuleEntity;
import com.mtalaat.restaurant.modules.auth.entity.ModulePermission;
import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.auth.mapping.ModulePermissionMapper;
import com.mtalaat.restaurant.modules.auth.repository.ModulePermissionRepository;
import com.mtalaat.restaurant.modules.auth.repository.ModuleRepository;
import com.mtalaat.restaurant.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModulePermissionService {

    private final ModulePermissionRepository modulePermissionRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final ModulePermissionMapper modulePermissionMapper;

    public ModulePermissionDto create(ModulePermissionDto dto) {
        ModuleEntity module = moduleRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + dto.getModuleId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        ModulePermission entity = new ModulePermission();
        entity.setModule(module);
        entity.setUser(user);
        entity.setCanCreate(dto.getCanCreate());
        entity.setCanRead(dto.getCanRead());
        entity.setCanUpdate(dto.getCanUpdate());
        entity.setCanDelete(dto.getCanDelete());

        return modulePermissionMapper.toDto(modulePermissionRepository.save(entity));
    }

    public ModulePermissionDto getById(Long id) {
        ModulePermission entity = modulePermissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module permission not found with id: " + id));
        return modulePermissionMapper.toDto(entity);
    }

    public List<ModulePermissionDto> getAll() {
        return modulePermissionRepository.findAll().stream().map(modulePermissionMapper::toDto).toList();
    }

    public ModulePermissionDto update(Long id, ModulePermissionDto dto) {
        ModulePermission existing = modulePermissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module permission not found with id: " + id));

        ModuleEntity module = moduleRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + dto.getModuleId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        existing.setModule(module);
        existing.setUser(user);
        existing.setCanCreate(dto.getCanCreate());
        existing.setCanRead(dto.getCanRead());
        existing.setCanUpdate(dto.getCanUpdate());
        existing.setCanDelete(dto.getCanDelete());

        return modulePermissionMapper.toDto(modulePermissionRepository.save(existing));
    }

    public void delete(Long id) {
        ModulePermission existing = modulePermissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module permission not found with id: " + id));
        modulePermissionRepository.delete(existing);
    }
}
