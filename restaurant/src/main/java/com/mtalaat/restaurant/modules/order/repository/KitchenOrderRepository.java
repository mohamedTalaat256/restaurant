package com.mtalaat.restaurant.modules.order.repository;

import com.mtalaat.restaurant.modules.order.entity.KitchenOrder;
import com.mtalaat.restaurant.modules.order.enums.KitchenOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, Long> {

    List<KitchenOrder> findByKitchenId(Long kitchenId);

    List<KitchenOrder> findByKitchenIdAndStatus(Long kitchenId, KitchenOrderStatus status);

    List<KitchenOrder> findByOrderId(Long orderId);
}
