package com.mtalaat.restaurant.modules.delivery.mapping;

import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.delivery.dto.DeliveryDetailsDto;
import com.mtalaat.restaurant.modules.delivery.entity.DeliveryDetails;
import org.springframework.stereotype.Component;

@Component
public class DeliveryDetailsMapper {

    public DeliveryDetailsDto toDto(DeliveryDetails entity) {
        User user = entity.getUser();
        return DeliveryDetailsDto.builder()
                .id(entity.getId())
                .userId(user != null ? user.getId() : null)
                .firstname(user != null ? user.getFirstname() : null)
                .lastname(user != null ? user.getLastname() : null)
                .email(user != null ? user.getEmail() : null)
                .password(null)
                .image(user != null ? user.getImage() : null)
                .phone(entity.getPhone())
                .vehicleType(entity.getVehicleType())
                .vehicleNumber(entity.getVehicleNumber())
                .status(entity.getStatus())
                .build();
    }

    public DeliveryDetails toEntity(DeliveryDetailsDto dto) {
        DeliveryDetails entity = new DeliveryDetails();
        entity.setPhone(dto.getPhone());
        entity.setVehicleType(dto.getVehicleType());
        entity.setVehicleNumber(dto.getVehicleNumber());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}
