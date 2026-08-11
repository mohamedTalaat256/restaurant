package com.mtalaat.restaurant.modules.delivery.mapper;

import com.mtalaat.restaurant.modules.delivery.dto.response.DriverResponse;
import com.mtalaat.restaurant.modules.delivery.entity.Driver;
import org.springframework.stereotype.Component;

/**
 * Manual mapper for {@link Driver} → {@link DriverResponse}.
 */
@Component
public class DriverMapper {

    public DriverResponse toResponse(Driver driver) {
        String fullName = driver.getUser() != null
                ? driver.getUser().getFirstname() + " " + driver.getUser().getLastname()
                : null;
        String email = driver.getUser() != null ? driver.getUser().getEmail() : null;

        return new DriverResponse(
                driver.getId(),
                driver.getUser() != null ? driver.getUser().getId() : null,
                fullName,
                email,
                driver.getPhone(),
                driver.getVehicleType(),
                driver.getVehiclePlate(),
                driver.getIsOnline(),
                driver.getStatus(),
                driver.getCreatedAt()
        );
    }
}
