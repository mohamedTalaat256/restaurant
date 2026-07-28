package com.mtalaat.restaurant.modules.auth.repository;

import com.mtalaat.restaurant.modules.auth.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccessRepository extends JpaRepository<UserRole, Long> {
}
