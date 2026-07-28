package com.mtalaat.restaurant.modules.foodManagement.repository;

import com.mtalaat.restaurant.modules.foodManagement.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
}
