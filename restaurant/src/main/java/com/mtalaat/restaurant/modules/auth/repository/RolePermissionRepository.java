package com.mtalaat.restaurant.modules.auth.repository;

import com.mtalaat.restaurant.modules.auth.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    //find all permissions for list of role ids
    List<RolePermission> findByRoleIdIn(List<Long> roleIds);

    //find all permissions by menu item id
    Optional<RolePermission> findByMenuItemId(Long menuItemId);
}
