package com.mtalaat.restaurant.modules.auth.service;

import com.mtalaat.restaurant.modules.auth.dto.UserAccessDto;
import com.mtalaat.restaurant.modules.auth.entity.Role;
import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.auth.entity.UserRole;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.auth.mapping.UserAccessMapper;
import com.mtalaat.restaurant.modules.auth.repository.RoleRepository;
import com.mtalaat.restaurant.modules.auth.repository.UserAccessRepository;
import com.mtalaat.restaurant.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAccessService {

    private final UserAccessRepository userAccessRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserAccessMapper userAccessMapper;

    public UserAccessDto create(UserAccessDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        UserRole entity = new UserRole();
        entity.setUser(user);
        entity.setRole(role);

        return userAccessMapper.toDto(userAccessRepository.save(entity));
    }

    public UserAccessDto getById(Long id) {
        UserRole entity = userAccessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User access not found with id: " + id));
        return userAccessMapper.toDto(entity);
    }

    public List<UserAccessDto> getAll() {
        return userAccessRepository.findAll().stream().map(userAccessMapper::toDto).toList();
    }

    public UserAccessDto update(Long id, UserAccessDto dto) {
        UserRole existing = userAccessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User access not found with id: " + id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        existing.setUser(user);
        existing.setRole(role);

        return userAccessMapper.toDto(userAccessRepository.save(existing));
    }

    public void delete(Long id) {
        UserRole existing = userAccessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User access not found with id: " + id));
        userAccessRepository.delete(existing);
    }
}
