package com.mtalaat.restaurant.modules.auth.service;

import com.mtalaat.restaurant.modules.auth.dto.RolePermissionDto;
import com.mtalaat.restaurant.modules.auth.dto.SideMenuItemDto;
import com.mtalaat.restaurant.modules.auth.entity.MenuItem;
import com.mtalaat.restaurant.modules.auth.entity.Role;
import com.mtalaat.restaurant.modules.auth.entity.RolePermission;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.auth.mapping.RolePermissionMapper;
import com.mtalaat.restaurant.modules.auth.repository.MenuItemRepository;
import com.mtalaat.restaurant.modules.auth.repository.RolePermissionRepository;
import com.mtalaat.restaurant.modules.auth.repository.RoleRepository;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final MenuItemRepository menuItemRepository;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionChecker permissionChecker;


    public RolePermissionDto create(RolePermissionDto dto) {
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        MenuItem menuItem = null;
        if (dto.getMenuItemId() != null) {
            menuItem = menuItemRepository.findById(dto.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + dto.getMenuItemId()));
        }

        RolePermission entity = new RolePermission();
        entity.setRole(role);
        entity.setMenuItem(menuItem);
        entity.setCanRead(dto.getCanRead());
        entity.setCanCreate(dto.getCanCreate());
        entity.setCanEdit(dto.getCanEdit());
        entity.setCanDelete(dto.getCanDelete());

        return rolePermissionMapper.toDto(rolePermissionRepository.save(entity));
    }

    public RolePermissionDto getById(Long id) {
        RolePermission entity = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role permission not found with id: " + id));
        return rolePermissionMapper.toDto(entity);
    }

    public List<RolePermissionDto> getAll() {
        return rolePermissionRepository.findAll().stream().map(rolePermissionMapper::toDto).toList();
    }

    public RolePermissionDto getByMenuItem(Long menuItemId) {
        return rolePermissionMapper.toDto(rolePermissionRepository.findByMenuItemId(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Role permission not found for menu item id: " + menuItemId)));
    }

    public List<RolePermissionDto> getByAuthUserRoles() {
        List<Long> roleIds = permissionChecker.getCurrentUser().getUserRoles().stream().map(r -> r.getRole().getId()).toList();

        return rolePermissionRepository.findByRoleIdIn(roleIds).stream().map(rolePermissionMapper::toDto).toList();
    }



    public RolePermissionDto update(Long id, RolePermissionDto dto) {
        RolePermission existing = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role permission not found with id: " + id));

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        MenuItem menuItem = null;
        if (dto.getMenuItemId() != null) {
            menuItem = menuItemRepository.findById(dto.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + dto.getMenuItemId()));
        }

        existing.setRole(role);
        existing.setMenuItem(menuItem);
        existing.setCanRead(dto.getCanRead());
        existing.setCanCreate(dto.getCanCreate());
        existing.setCanEdit(dto.getCanEdit());
        existing.setCanDelete(dto.getCanDelete());

        return rolePermissionMapper.toDto(rolePermissionRepository.save(existing));
    }

    public void delete(Long id) {
        RolePermission existing = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role permission not found with id: " + id));
        rolePermissionRepository.delete(existing);
    }

    public List<RolePermission> getRolePermissionsForRoles(List<Long> rolesId) {
        return rolePermissionRepository.findByRoleIdIn(rolesId);
    }

    public List<SideMenuItemDto> buildSideMenuForRolePermissions(List<RolePermission> rolePermissions) {

        List<Long> menuIds = rolePermissions.stream()
                .filter(rp -> rp.getMenuItem() != null && Boolean.TRUE.equals(rp.getCanRead()))
                .map(rp -> rp.getMenuItem().getId())
                .toList();

        //parent menu that have no parent (parentMenu = NULL)
        List<SideMenuItemDto> parentMenuItems = rolePermissions.stream()
                .filter(rp -> rp.getMenuItem() != null
                        && rp.getMenuItem().getParentMenu() == null
                        && menuIds.contains(rp.getMenuItem().getId())
                        && Boolean.TRUE.equals(rp.getCanRead()))
                .map(rp -> SideMenuItemDto.builder()
                        .label(rp.getMenuItem().getTitle())
                        .icon(rp.getMenuItem().getModule().getIcon())
                        .routerLink(rp.getMenuItem().getPageUrl())
                        .isPage(rp.getMenuItem().getIsPage())
                        .items(
                                rolePermissions.stream()
                                        .filter(childRp -> childRp.getMenuItem() != null
                                                && childRp.getMenuItem().getParentMenu() != null
                                                && childRp.getMenuItem().getParentMenu().getId().equals(rp.getMenuItem().getId())
                                                && menuIds.contains(childRp.getMenuItem().getId())
                                                && Boolean.TRUE.equals(childRp.getCanRead()))
                                        .map(childRp -> SideMenuItemDto.builder()
                                                .label(childRp.getMenuItem().getTitle())
                                                .icon(childRp.getMenuItem().getModule().getIcon())
                                                .routerLink(childRp.getMenuItem().getPageUrl())
                                                .isPage(childRp.getMenuItem().getIsPage())
                                                .build())
                                        .toList()
                        )
                        .build())
                .distinct()
                .toList();





        return parentMenuItems;
    }


    public RolePermissionDto updateOrCreate( RolePermissionDto dto) {
        if (dto.getId() == null) {
            return create(dto);
        } else {
            return update(dto.getId(), dto);
        }
    }
}
