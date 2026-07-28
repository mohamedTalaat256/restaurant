package com.mtalaat.restaurant.modules.auth.repository;

import com.mtalaat.restaurant.modules.auth.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
