package com.mtalaat.restaurant.modules.auth.mapping;

import com.mtalaat.restaurant.modules.auth.dto.MenuItemDto;
import com.mtalaat.restaurant.modules.auth.dto.RolePermissionDto;
import com.mtalaat.restaurant.modules.auth.entity.MenuItem;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MenuItemMapper {

    public MenuItemDto toDto(MenuItem entity) {

        Set<RolePermissionDto> rolePermissionDtoSet =
                entity.getRolePermissions() == null
                        ? Collections.emptySet()
                        : entity.getRolePermissions().stream()
                        .map(rp -> RolePermissionDto.builder()
                                .id(rp.getId())
                                .roleId(rp.getRole() != null ? rp.getRole().getId() : null)
                                .menuItemId(rp.getMenuItem() != null ? rp.getMenuItem().getId() : null)
                                .canRead(rp.getCanRead())
                                .canCreate(rp.getCanCreate())
                                .canEdit(rp.getCanEdit())
                                .canDelete(rp.getCanDelete())
                                .build())
                        .collect(Collectors.toSet());

        return MenuItemDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .isPage(entity.getIsPage())
                .pageUrl(entity.getPageUrl())
                .moduleId(entity.getModule() != null ? entity.getModule().getId() : null)
                .parentMenuId(entity.getParentMenu() != null ? entity.getParentMenu().getId() : null)
                .isReport(entity.getIsReport())
                .rolePermissions(rolePermissionDtoSet)
                .build();
    }

    public MenuItem toEntity(MenuItemDto dto) {
        MenuItem menuItem = new MenuItem();
        menuItem.setTitle(dto.getTitle());
        menuItem.setIsPage(dto.getIsPage());
        menuItem.setPageUrl(dto.getPageUrl());
        menuItem.setIsReport(dto.getIsReport());
        return menuItem;
    }
}
