package com.mtalaat.restaurant.modules.settings.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private Boolean status;
}
