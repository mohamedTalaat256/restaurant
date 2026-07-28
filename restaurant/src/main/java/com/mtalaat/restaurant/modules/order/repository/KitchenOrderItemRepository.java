package com.mtalaat.restaurant.modules.order.repository;

import com.mtalaat.restaurant.modules.order.entity.KitchenOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KitchenOrderItemRepository extends JpaRepository<KitchenOrderItem, Long> {

    List<KitchenOrderItem> findByKitchenOrderId(Long kitchenOrderId);
}
