package com.mtalaat.restaurant.modules.order.repository;

import com.mtalaat.restaurant.modules.order.entity.Order;
import com.mtalaat.restaurant.modules.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAll(Pageable pageable);
    Page<Order> findByStatusIn(Pageable pageable, List<OrderStatus> statuses);

    Page<Order> findByStatus(Pageable pageable,OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.table.id = :tableId AND o.status NOT IN ('COMPLETED', 'CHECKED_OUT', 'CANCELLED', 'MERGED')")
    List<Order> findActiveOrdersByTableId(@Param("tableId") Long tableId);

    boolean existsByOrderNumber(String orderNumber);
}
