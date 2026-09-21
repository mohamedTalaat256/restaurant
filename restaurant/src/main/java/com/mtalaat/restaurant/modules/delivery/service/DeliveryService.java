package com.mtalaat.restaurant.modules.delivery.service;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.auth.entity.Role;
import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.auth.entity.UserRole;
import com.mtalaat.restaurant.modules.auth.repository.RoleRepository;
import com.mtalaat.restaurant.modules.auth.repository.UserRepository;
import com.mtalaat.restaurant.modules.delivery.dto.DeliveryDetailsDto;
import com.mtalaat.restaurant.modules.delivery.entity.DeliveryDetails;
import com.mtalaat.restaurant.modules.delivery.mapping.DeliveryDetailsMapper;
import com.mtalaat.restaurant.modules.delivery.repository.DeliveryDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private static final String DELIVERY_ROLE = "DELIVERY";

    private final DeliveryDetailsRepository deliveryDetailsRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DeliveryDetailsMapper deliveryDetailsMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public DeliveryDetailsDto create(DeliveryDetailsDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("msg_delivery_email_exists");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BadRequestException("msg_delivery_password_required");
        }

        // Create the underlying system user with the DELIVERY role
        User user = User.builder()
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .image(dto.getImage())
                .status(dto.getStatus() != null ? dto.getStatus() : true)
                .isAdmin(false)
                .build();

        assignDeliveryRole(user);
        user = userRepository.save(user);

        DeliveryDetails details = deliveryDetailsMapper.toEntity(dto);
        details.setUser(user);
        if (details.getStatus() == null) {
            details.setStatus(true);
        }
        details = deliveryDetailsRepository.save(details);

        return deliveryDetailsMapper.toDto(details);
    }

    public DeliveryDetailsDto getById(Long id) {
        DeliveryDetails details = findOrThrow(id);
        return deliveryDetailsMapper.toDto(details);
    }

    public List<DeliveryDetailsDto> getAll() {
        return deliveryDetailsRepository.findAll().stream()
                .map(deliveryDetailsMapper::toDto)
                .toList();
    }

    public List<DeliveryDetailsDto> getByStatus(Boolean status) {
        return deliveryDetailsRepository.findByStatus(status).stream()
                .map(deliveryDetailsMapper::toDto)
                .toList();
    }

    @Transactional
    public DeliveryDetailsDto update(Long id, DeliveryDetailsDto dto) {
        DeliveryDetails details = findOrThrow(id);
        User user = details.getUser();

        // Keep the users table in sync with the basic details edited here
        if (user != null) {
            if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
                throw new BadRequestException("msg_delivery_email_exists");
            }
            user.setFirstname(dto.getFirstname());
            user.setLastname(dto.getLastname());
            user.setEmail(dto.getEmail());
            if (dto.getImage() != null && !dto.getImage().isBlank()) {
                user.setImage(dto.getImage());
            }
            if (dto.getStatus() != null) {
                user.setStatus(dto.getStatus());
            }
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            userRepository.save(user);
        }

        // Update delivery specific details
        details.setPhone(dto.getPhone());
        details.setVehicleType(dto.getVehicleType());
        details.setVehicleNumber(dto.getVehicleNumber());
        if (dto.getStatus() != null) {
            details.setStatus(dto.getStatus());
        }

        DeliveryDetails saved = deliveryDetailsRepository.save(details);
        return deliveryDetailsMapper.toDto(saved);
    }

    @Transactional
    public DeliveryDetailsDto updateStatus(Long id, Boolean status) {
        DeliveryDetails details = findOrThrow(id);
        details.setStatus(status);
        if (details.getUser() != null) {
            details.getUser().setStatus(status);
            userRepository.save(details.getUser());
        }
        DeliveryDetails saved = deliveryDetailsRepository.save(details);
        return deliveryDetailsMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        DeliveryDetails details = findOrThrow(id);
        User user = details.getUser();
        deliveryDetailsRepository.delete(details);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    private DeliveryDetails findOrThrow(Long id) {
        return deliveryDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("msg_delivery_not_found"));
    }

    private void assignDeliveryRole(User user) {
        Role role = roleRepository.findByName(DELIVERY_ROLE)
                .orElseThrow(() -> new ResourceNotFoundException("msg_delivery_role_not_found"));
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        user.getUserRoles().add(userRole);
    }
}
