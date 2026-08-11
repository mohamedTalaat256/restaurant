package com.mtalaat.restaurant.modules.delivery.mapper;

import com.mtalaat.restaurant.modules.delivery.dto.response.DeliveryResponse;
import com.mtalaat.restaurant.modules.delivery.entity.Delivery;
import com.mtalaat.restaurant.modules.delivery.entity.Driver;
import org.springframework.stereotype.Component;

/**
 * Manual mapper for {@link Delivery} → {@link DeliveryResponse}.
 *
 * Uses manual mapping (Spring @Component) to stay consistent with the rest of
 * the codebase and to give explicit control over nested object mapping.
 */
@Component
public class DeliveryMapper {

    public DeliveryResponse toResponse(Delivery delivery) {
        DeliveryResponse.DriverSummary driverSummary = buildDriverSummary(delivery.getDriver());

        return new DeliveryResponse(
                delivery.getId(),
                delivery.getOrder() != null ? delivery.getOrder().getId() : null,
                delivery.getOrder() != null ? delivery.getOrder().getOrderNumber() : null,
                driverSummary,
                delivery.getStatus(),
                delivery.getDeliveryFee(),
                delivery.getEstimatedDeliveryTime(),
                delivery.getActualDeliveryTime(),
                delivery.getAssignedAt(),
                delivery.getAcceptedAt(),
                delivery.getPickedUpAt(),
                delivery.getDeliveredAt(),
                delivery.getCancelledAt(),
                delivery.getNotes(),
                delivery.getCreatedAt(),
                delivery.getUpdatedAt()
        );
    }

    private DeliveryResponse.DriverSummary buildDriverSummary(Driver driver) {
        if (driver == null) return null;
        String fullName = driver.getUser() != null
                ? driver.getUser().getFirstname() + " " + driver.getUser().getLastname()
                : null;
        return new DeliveryResponse.DriverSummary(
                driver.getId(),
                fullName,
                driver.getPhone(),
                driver.getVehiclePlate(),
                driver.getVehicleType() != null ? driver.getVehicleType().name() : null
        );
    }
}
