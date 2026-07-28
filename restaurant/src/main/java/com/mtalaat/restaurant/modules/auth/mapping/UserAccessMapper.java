package com.mtalaat.restaurant.modules.auth.mapping;

import com.mtalaat.restaurant.modules.auth.dto.UserAccessDto;
import com.mtalaat.restaurant.modules.auth.entity.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserAccessMapper {

    public UserAccessDto toDto(UserRole entity) {
        return UserAccessDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .roleId(entity.getRole().getId())
                .build();
    }
}
