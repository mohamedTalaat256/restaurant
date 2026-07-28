package com.mtalaat.restaurant.modules.auth.mapping;

import com.mtalaat.restaurant.modules.auth.dto.UserDto;
import com.mtalaat.restaurant.modules.auth.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User entity) {
        Set<Long> roleIds = entity.getUserRoles() == null
                ? new HashSet<>()
                : entity.getUserRoles().stream()
                .map(userAccess -> userAccess.getRole().getId())
                .collect(Collectors.toSet());

        return UserDto.builder()
                .id(entity.getId())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .about(entity.getAbout())
                .waiterKitchenToken(entity.getWaiterKitchenToken())
                .email(entity.getEmail())
                .password(null)
                .image(entity.getImage())
                .lastLogin(entity.getLastLogin())
                .lastLogout(entity.getLastLogout())
                .ipAddress(entity.getIpAddress())
                .counter(entity.getCounter())
                .status(entity.getStatus())
                .isAdmin(entity.getIsAdmin())
                .roleIds(roleIds)
                .build();
    }

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setAbout(dto.getAbout());
        user.setWaiterKitchenToken(dto.getWaiterKitchenToken());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setImage(dto.getImage());
        user.setLastLogin(dto.getLastLogin());
        user.setLastLogout(dto.getLastLogout());
        user.setIpAddress(dto.getIpAddress());
        user.setCounter(dto.getCounter());
        user.setStatus(dto.getStatus());
        user.setIsAdmin(dto.getIsAdmin());
        return user;
    }
}
