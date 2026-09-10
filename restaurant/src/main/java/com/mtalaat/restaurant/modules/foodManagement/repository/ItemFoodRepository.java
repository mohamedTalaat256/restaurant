package com.mtalaat.restaurant.modules.foodManagement.repository;

import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemFoodRepository extends JpaRepository<ItemFood, Long> {
}
