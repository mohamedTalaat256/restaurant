package com.mtalaat.restaurant.modules.purchase.repository;

import com.mtalaat.restaurant.modules.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
