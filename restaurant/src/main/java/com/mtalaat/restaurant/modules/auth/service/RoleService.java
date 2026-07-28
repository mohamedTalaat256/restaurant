package com.mtalaat.restaurant.modules.auth.service;

import com.mtalaat.restaurant.modules.auth.dto.RoleDto;
import com.mtalaat.restaurant.modules.auth.entity.Role;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.auth.mapping.RoleMapper;
import com.mtalaat.restaurant.modules.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDto create(RoleDto dto) {
        Role role = roleMapper.toEntity(dto);
        return roleMapper.toDto(roleRepository.save(role));
    }

    public RoleDto getById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        return roleMapper.toDto(role);
    }

    public List<RoleDto> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toDto).toList();
    }

    public RoleDto update(Long id, RoleDto dto) {
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        return roleMapper.toDto(roleRepository.save(existing));
    }

    public void delete(Long id) {
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        roleRepository.delete(existing);
    }
}
