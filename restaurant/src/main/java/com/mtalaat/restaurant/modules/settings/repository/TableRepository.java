package com.mtalaat.restaurant.modules.settings.repository;

import com.mtalaat.restaurant.modules.settings.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
}
