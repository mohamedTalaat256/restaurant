package com.mtalaat.restaurant.modules.delivery.service;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.auth.repository.UserRepository;
import com.mtalaat.restaurant.modules.delivery.dto.request.ChangeDriverStatusRequest;
import com.mtalaat.restaurant.modules.delivery.dto.response.DeliveryResponse;
import com.mtalaat.restaurant.modules.delivery.dto.response.DriverResponse;
import com.mtalaat.restaurant.modules.delivery.entity.Driver;
import com.mtalaat.restaurant.modules.delivery.enums.DriverStatus;
import com.mtalaat.restaurant.modules.delivery.mapper.DeliveryMapper;
import com.mtalaat.restaurant.modules.delivery.mapper.DriverMapper;
import com.mtalaat.restaurant.modules.delivery.repository.DeliveryRepository;
import com.mtalaat.restaurant.modules.delivery.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Manages driver profiles and availability.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final DriverMapper driverMapper;
    private final DeliveryMapper deliveryMapper;

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<DriverResponse> getAvailableDrivers() {
        return driverRepository.findAvailableDrivers().stream()
                .map(driverMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DriverResponse getDriverById(Long driverId) {
        return driverMapper.toResponse(findDriverById(driverId));
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(driverMapper::toResponse)
                .toList();
    }

    // ─────────────────────────────────────────────
    // STATUS
    // ─────────────────────────────────────────────

    /**
     * Changes the availability status of a driver.
     *
     * <p>Business rules:</p>
     * <ul>
     *   <li>A BUSY driver cannot manually go OFFLINE or ONLINE until the delivery is completed.</li>
     * </ul>
     */
    public DriverResponse changeDriverStatus(Long driverId, ChangeDriverStatusRequest request) {
        Driver driver = findDriverById(driverId);

        if (driver.getStatus() == DriverStatus.BUSY) {
            throw new BadRequestException("msg_driver_busy_cannot_change_status");
        }

        driver.setStatus(request.status());
        driver.setIsOnline(request.status() == DriverStatus.ONLINE);

        return driverMapper.toResponse(driverRepository.save(driver));
    }

    // ─────────────────────────────────────────────
    // CURRENT DELIVERY
    // ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public DeliveryResponse getDriverCurrentDelivery(Long driverId) {
        findDriverById(driverId); // validate driver exists
        return deliveryRepository.findActiveDeliveryByDriverId(driverId)
                .map(deliveryMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("msg_no_active_delivery"));
    }

    // ─────────────────────────────────────────────
    // DELIVERY HISTORY
    // ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<DeliveryResponse> getDriverDeliveryHistory(Long driverId) {
        findDriverById(driverId); // validate driver exists
        return deliveryRepository.findDeliveryHistoryByDriverId(driverId).stream()
                .map(deliveryMapper::toResponse)
                .toList();
    }

    // ─────────────────────────────────────────────
    // INTERNAL HELPERS
    // ─────────────────────────────────────────────

    private Driver findDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("msg_driver_not_found"));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("msg_user_not_found"));
    }
}
