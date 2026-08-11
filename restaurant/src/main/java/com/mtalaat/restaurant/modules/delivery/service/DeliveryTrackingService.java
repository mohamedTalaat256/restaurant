package com.mtalaat.restaurant.modules.delivery.service;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.delivery.dto.request.UpdateDriverLocationRequest;
import com.mtalaat.restaurant.modules.delivery.dto.response.DeliveryTrackingResponse;
import com.mtalaat.restaurant.modules.delivery.entity.Delivery;
import com.mtalaat.restaurant.modules.delivery.entity.DeliveryTracking;
import com.mtalaat.restaurant.modules.delivery.enums.DeliveryStatus;
import com.mtalaat.restaurant.modules.delivery.mapper.DeliveryTrackingMapper;
import com.mtalaat.restaurant.modules.delivery.repository.DeliveryRepository;
import com.mtalaat.restaurant.modules.delivery.repository.DeliveryTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Manages append-only GPS tracking records for active deliveries.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryTrackingService {

    private final DeliveryTrackingRepository trackingRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryTrackingMapper trackingMapper;

    // ─────────────────────────────────────────────
    // RECORD LOCATION
    // ─────────────────────────────────────────────

    /**
     * Appends a new GPS point to the delivery's tracking trail.
     *
     * <p>Business rule: location updates are only accepted for deliveries in
     * ACCEPTED, ARRIVED_AT_RESTAURANT, PICKED_UP, or ON_THE_WAY states.</p>
     */
    public DeliveryTrackingResponse recordLocation(Long deliveryId, UpdateDriverLocationRequest request) {
        Delivery delivery = findDeliveryById(deliveryId);
        validateActiveForTracking(delivery);

        DeliveryTracking tracking = DeliveryTracking.builder()
                .delivery(delivery)
                .latitude(request.latitude())
                .longitude(request.longitude())
                .speed(request.speed())
                .heading(request.heading())
                .build();

        return trackingMapper.toResponse(trackingRepository.save(tracking));
    }

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    /**
     * Returns the full GPS trail for a delivery in chronological order.
     */
    @Transactional(readOnly = true)
    public List<DeliveryTrackingResponse> getDeliveryTracking(Long deliveryId) {
        findDeliveryById(deliveryId); // validate delivery exists
        return trackingRepository.findByDeliveryIdOrderByCreatedAtAsc(deliveryId).stream()
                .map(trackingMapper::toResponse)
                .toList();
    }

    /**
     * Returns only the most recent location point (current driver position).
     */
    @Transactional(readOnly = true)
    public DeliveryTrackingResponse getLatestLocation(Long deliveryId) {
        findDeliveryById(deliveryId);
        return trackingRepository.findTopByDeliveryIdOrderByCreatedAtDesc(deliveryId)
                .map(trackingMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("msg_no_tracking_data"));
    }

    // ─────────────────────────────────────────────
    // INTERNAL HELPERS
    // ─────────────────────────────────────────────

    private Delivery findDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("msg_delivery_not_found"));
    }

    private void validateActiveForTracking(Delivery delivery) {
        DeliveryStatus status = delivery.getStatus();
        if (status == DeliveryStatus.ACCEPTED
                || status == DeliveryStatus.ARRIVED_AT_RESTAURANT
                || status == DeliveryStatus.PICKED_UP
                || status == DeliveryStatus.ON_THE_WAY) {
            return;
        }
        throw new BadRequestException("msg_delivery_not_active_for_tracking");
    }
}
