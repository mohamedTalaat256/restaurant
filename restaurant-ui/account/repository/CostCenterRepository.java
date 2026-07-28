package com.mtalaat.restaurant.modules.account.repository;

import com.mtalaat.restaurant.modules.account.entity.CostCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, Long> {

    Optional<CostCenter> findByCode(String code);

    List<CostCenter> findByStatusTrue();

    boolean existsByCode(String code);
}
