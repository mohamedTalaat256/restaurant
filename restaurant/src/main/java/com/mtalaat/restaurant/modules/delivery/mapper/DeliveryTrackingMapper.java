package com.mtalaat.restaurant.modules.delivery.mapper;

import com.mtalaat.restaurant.modules.delivery.dto.response.DeliveryTrackingResponse;
import com.mtalaat.restaurant.modules.delivery.entity.DeliveryTracking;
import org.springframework.stereotype.Component;

/**
 * Manual mapper for {@link DeliveryTracking} → {@link DeliveryTrackingResponse}.
 */
@Component
public class DeliveryTrackingMapper {

    public DeliveryTrackingResponse toResponse(DeliveryTracking tracking) {
        return new DeliveryTrackingResponse(
                tracking.getId(),
                tracking.getDelivery() != null ? tracking.getDelivery().getId() : null,
                tracking.getLatitude(),
                tracking.getLongitude(),
                tracking.getSpeed(),
                tracking.getHeading(),
                tracking.getCreatedAt()
        );
    }
}
