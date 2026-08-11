package com.mtalaat.restaurant.modules.delivery.repository;

import com.mtalaat.restaurant.modules.delivery.entity.Delivery;
import com.mtalaat.restaurant.modules.delivery.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);

    List<Delivery> findByDriverId(Long driverId);

    List<Delivery> findByStatus(DeliveryStatus status);

    /**
     * Returns the currently active delivery for a given driver.
     * Active = any status between ASSIGNED and ON_THE_WAY.
     */
    @Query("""
            SELECT d FROM Delivery d
            WHERE d.driver.id = :driverId
              AND d.status IN ('ASSIGNED', 'ACCEPTED', 'ARRIVED_AT_RESTAURANT', 'PICKED_UP', 'ON_THE_WAY')
            """)
    Optional<Delivery> findActiveDeliveryByDriverId(@Param("driverId") Long driverId);

    /**
     * Delivery history for a driver (terminal states only).
     */
    @Query("""
            SELECT d FROM Delivery d
            WHERE d.driver.id = :driverId
              AND d.status IN ('DELIVERED', 'FAILED', 'CANCELLED')
            ORDER BY d.createdAt DESC
            """)
    List<Delivery> findDeliveryHistoryByDriverId(@Param("driverId") Long driverId);
}
