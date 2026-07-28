package com.mtalaat.restaurant.modules.purchase.repository;

import com.mtalaat.restaurant.modules.purchase.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
