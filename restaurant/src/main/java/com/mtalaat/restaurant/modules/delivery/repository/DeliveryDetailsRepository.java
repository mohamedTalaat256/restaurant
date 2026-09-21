package com.mtalaat.restaurant.modules.delivery.repository;

import com.mtalaat.restaurant.modules.delivery.entity.DeliveryDetails;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryDetailsRepository extends JpaRepository<DeliveryDetails, Long> {

    @EntityGraph(attributePaths = {"user"})
    Optional<DeliveryDetails> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user"})
    List<DeliveryDetails> findByStatus(Boolean status);

    boolean existsByUserId(Long userId);
}
