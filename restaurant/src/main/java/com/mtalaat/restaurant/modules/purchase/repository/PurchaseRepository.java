package com.mtalaat.restaurant.modules.purchase.repository;

import com.mtalaat.restaurant.modules.purchase.entity.Purchase;
import com.mtalaat.restaurant.modules.purchase.enums.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    boolean existsBySupplierIdAndInvoiceNumberAndStatusNot(Long supplierId, String invoiceNumber, PurchaseStatus status);

    Optional<Purchase> findBySupplierIdAndInvoiceNumber(Long supplierId, String invoiceNumber);
}
