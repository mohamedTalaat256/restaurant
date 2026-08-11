package com.mtalaat.restaurant.modules.delivery.repository;

import com.mtalaat.restaurant.modules.delivery.entity.OrderDeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDeliveryAddressRepository extends JpaRepository<OrderDeliveryAddress, Long> {

    Optional<OrderDeliveryAddress> findByOrderId(Long orderId);
}
