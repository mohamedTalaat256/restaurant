package com.mtalaat.restaurant.modules.purchase.repository;

import com.mtalaat.restaurant.modules.purchase.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
}
