package com.mtalaat.restaurant.modules.auth.repository;

import com.mtalaat.restaurant.modules.auth.entity.ModulePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModulePermissionRepository extends JpaRepository<ModulePermission, Long> {
}
