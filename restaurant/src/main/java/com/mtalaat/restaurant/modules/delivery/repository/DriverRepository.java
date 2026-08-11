package com.mtalaat.restaurant.modules.delivery.repository;

import com.mtalaat.restaurant.modules.delivery.entity.Driver;
import com.mtalaat.restaurant.modules.delivery.enums.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUserId(Long userId);

    /** Returns all drivers currently available for assignment. */
    List<Driver> findByIsOnlineTrueAndStatus(DriverStatus status);

    /** Checks whether a user already has a driver profile. */
    boolean existsByUserId(Long userId);

    /**
     * Finds drivers who are ONLINE and not currently BUSY,
     * i.e. drivers that can accept a new delivery.
     */
    @Query("SELECT d FROM Driver d WHERE d.isOnline = true AND d.status = 'ONLINE'")
    List<Driver> findAvailableDrivers();
}
