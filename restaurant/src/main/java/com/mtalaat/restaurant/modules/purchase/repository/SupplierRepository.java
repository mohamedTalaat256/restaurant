package com.mtalaat.restaurant.modules.purchase.repository;

import com.mtalaat.restaurant.modules.purchase.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
