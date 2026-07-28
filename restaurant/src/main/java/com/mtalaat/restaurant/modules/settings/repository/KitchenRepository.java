package com.mtalaat.restaurant.modules.settings.repository;

import com.mtalaat.restaurant.modules.settings.entity.Kitchen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenRepository extends JpaRepository<Kitchen, Long> {
}
