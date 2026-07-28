package com.mtalaat.restaurant.modules.settings.repository;

import com.mtalaat.restaurant.modules.settings.entity.ThirdPartyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdPartyCustomerRepository extends JpaRepository<ThirdPartyCustomer, Long> {
}
