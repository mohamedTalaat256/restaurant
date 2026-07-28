package com.mtalaat.restaurant.modules.order.repository;

import com.mtalaat.restaurant.modules.order.entity.CashCounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashCounterRepository extends JpaRepository<CashCounter, Long> {
}
