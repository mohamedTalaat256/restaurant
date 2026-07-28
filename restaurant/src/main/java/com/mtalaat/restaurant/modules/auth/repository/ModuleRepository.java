package com.mtalaat.restaurant.modules.auth.repository;

import com.mtalaat.restaurant.modules.auth.entity.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {
}
