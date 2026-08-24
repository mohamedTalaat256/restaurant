package com.mtalaat.restaurant.modules.purchase.repository;

import com.mtalaat.restaurant.modules.purchase.entity.SupplierLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupplierLedgerRepository extends JpaRepository<SupplierLedger, Long> {

    /** آخر رصيد مسجل للمورد لحساب الرصيد التراكمي */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.supplier.id = :supplierId ORDER BY sl.id DESC LIMIT 1")
    Optional<SupplierLedger> findLatestBySupplier(@Param("supplierId") Long supplierId);
}
