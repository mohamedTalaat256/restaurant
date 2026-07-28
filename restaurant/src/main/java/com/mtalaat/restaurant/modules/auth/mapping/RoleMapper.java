package com.mtalaat.restaurant.modules.auth.mapping;

import com.mtalaat.restaurant.modules.auth.dto.RoleDto;
import com.mtalaat.restaurant.modules.auth.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDto toDto(Role entity) {
        return RoleDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build();
    }

    public Role toEntity(RoleDto dto) {
        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setStatus(dto.getStatus());
        return role;
    }
}
