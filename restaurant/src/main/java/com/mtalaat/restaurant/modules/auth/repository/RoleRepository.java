package com.mtalaat.restaurant.modules.auth.repository;

import com.mtalaat.restaurant.modules.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
