package com.mtalaat.restaurant.modules.settings.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String address;

    private String favoriteDeliveryAddress;

    private String password;

    @NotNull(message = "Customer type is required")
    private String customerType;

    private String customerTypeDescription;

    private Boolean status;

    private Boolean allowCredit;


    private Long accountId;
    private String accountCode;
    private String accountName;
}
