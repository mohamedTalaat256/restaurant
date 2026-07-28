package com.mtalaat.restaurant.modules.foodManagement.repository;

import com.mtalaat.restaurant.modules.foodManagement.entity.MenuType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuTypeRepository extends JpaRepository<MenuType, Long> {
}
