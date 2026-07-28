package com.mtalaat.restaurant.modules.account.repository;

import com.mtalaat.restaurant.modules.account.entity.FiscalPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FiscalPeriodRepository extends JpaRepository<FiscalPeriod, Long> {

    Optional<FiscalPeriod> findByYearAndMonth(Integer year, Integer month);

    List<FiscalPeriod> findByYearOrderByMonthAsc(Integer year);

    List<FiscalPeriod> findAllByOrderByYearDescMonthDesc();

    boolean existsByYearAndMonthAndLockedTrue(Integer year, Integer month);
}
