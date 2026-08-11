package com.mtalaat.restaurant.modules.delivery.repository;

import com.mtalaat.restaurant.modules.delivery.entity.DeliveryTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryTrackingRepository extends JpaRepository<DeliveryTracking, Long> {

    /** Returns all tracking points for a delivery ordered chronologically. */
    List<DeliveryTracking> findByDeliveryIdOrderByCreatedAtAsc(Long deliveryId);

    /** Returns the most recent tracking point for a delivery (latest driver position). */
    Optional<DeliveryTracking> findTopByDeliveryIdOrderByCreatedAtDesc(Long deliveryId);
}
