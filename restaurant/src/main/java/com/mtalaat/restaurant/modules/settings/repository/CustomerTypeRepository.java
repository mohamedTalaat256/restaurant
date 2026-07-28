package com.mtalaat.restaurant.modules.settings.repository;

import com.mtalaat.restaurant.modules.settings.entity.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerTypeRepository extends JpaRepository<CustomerType, String> {
}
