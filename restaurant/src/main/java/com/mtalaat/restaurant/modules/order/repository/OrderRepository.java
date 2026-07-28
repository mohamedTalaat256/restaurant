package com.mtalaat.restaurant.modules.order.repository;

import com.mtalaat.restaurant.modules.order.entity.Order;
import com.mtalaat.restaurant.modules.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusIn(List<OrderStatus> statuses);

    List<Order> findByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.table.id = :tableId AND o.status NOT IN ('COMPLETED', 'CHECKED_OUT', 'CANCELLED', 'MERGED')")
    List<Order> findActiveOrdersByTableId(@Param("tableId") Long tableId);

    boolean existsByOrderNumber(String orderNumber);
}
