package com.mtalaat.restaurant.modules.settings.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyCustomerDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String address;

    private String phone;

    private String email;

    @NotNull(message = "Commission percentage is required")
    private Double commissionPercentage;
}
