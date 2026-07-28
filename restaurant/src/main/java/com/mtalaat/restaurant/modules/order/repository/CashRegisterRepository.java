package com.mtalaat.restaurant.modules.order.repository;

import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.order.entity.CashRegister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashRegisterRepository extends JpaRepository<CashRegister, Long> {

    Optional<CashRegister> findByUserAndStatus(User user, Boolean status);
}
