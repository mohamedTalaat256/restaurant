package com.mtalaat.restaurant.modules.delivery.repository;

import com.mtalaat.restaurant.modules.delivery.entity.DeliveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Long> {

    List<DeliveryAssignment> findByDeliveryIdOrderByAssignedAtDesc(Long deliveryId);
}
