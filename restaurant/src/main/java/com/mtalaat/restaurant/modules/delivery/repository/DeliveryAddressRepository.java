package com.mtalaat.restaurant.modules.delivery.repository;

import com.mtalaat.restaurant.modules.delivery.entity.DeliveryAddress;
import com.mtalaat.restaurant.modules.settings.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

    List<DeliveryAddress> findByCustomerAndActiveTrue(Customer customer);

    Optional<DeliveryAddress> findByCustomerAndIsDefaultTrue(Customer customer);
}
