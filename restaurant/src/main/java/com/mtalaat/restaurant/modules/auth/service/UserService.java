package com.mtalaat.restaurant.modules.auth.service;

import com.mtalaat.restaurant.modules.auth.dto.UserDto;
import com.mtalaat.restaurant.modules.auth.entity.Role;
import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.auth.entity.UserRole;
import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.auth.mapping.UserMapper;
import com.mtalaat.restaurant.modules.auth.repository.RoleRepository;
import com.mtalaat.restaurant.modules.auth.repository.UserRepository;
import com.mtalaat.restaurant.utility.FileUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileUtility fileUtility;

    @Transactional
    public UserDto create(UserDto dto, MultipartFile image) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = userMapper.toEntity(dto);
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(true);
        }

        //save image and get path
        if (image != null && !image.isEmpty()) {
            String imagePath = fileUtility.saveFile(image, "users");
            user.setImage(imagePath);
        }

        user = userRepository.save(user);
        syncUserRoles(user, dto.getRoleIds());
        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Transactional
    public UserDto update(Long id, UserDto dto, MultipartFile image) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!existing.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        existing.setFirstname(dto.getFirstname());
        existing.setLastname(dto.getLastname());
        existing.setAbout(dto.getAbout());
        existing.setWaiterKitchenToken(dto.getWaiterKitchenToken());
        existing.setEmail(dto.getEmail());
        
         //override image if new image is provided
        if (image != null && !image.isEmpty()) {
            String imagePath = fileUtility.saveFile(image, "users");
            existing.setImage(imagePath);
        }

        existing.setLastLogin(dto.getLastLogin());
        existing.setLastLogout(dto.getLastLogout());
        existing.setIpAddress(dto.getIpAddress());
        existing.setCounter(dto.getCounter());
        existing.setStatus(dto.getStatus());
        existing.setIsAdmin(dto.getIsAdmin());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        syncUserRoles(existing, dto.getRoleIds());
        User saved = userRepository.save(existing);
        return userMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(existing);
    }

    private void syncUserRoles(User user, Set<Long> roleIds) {
        user.getUserRoles().clear();
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }

        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            user.getUserRoles().add(userRole);
        }
    }

}
