package com.mtalaat.restaurant.modules.delivery.dto;

import com.mtalaat.restaurant.modules.delivery.enums.VehicleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDetailsDto {

    private Long id;

    // Linked system user (Role DELIVERY)
    private Long userId;

    // ── Basic user details (kept in sync with users table) ──
    @NotBlank(message = "msg_delivery_firstname_required")
    private String firstname;

    @NotBlank(message = "msg_delivery_lastname_required")
    private String lastname;

    @NotBlank(message = "msg_delivery_email_required")
    @Email(message = "msg_delivery_email_invalid")
    private String email;

    // Only used on create / password change
    @Size(min = 6, message = "msg_delivery_password_min")
    private String password;

    private String image;

    // ── Delivery specific details ──
    @NotBlank(message = "msg_delivery_phone_required")
    private String phone;

    private VehicleType vehicleType;

    private String vehicleNumber;

    private Boolean status;
}
