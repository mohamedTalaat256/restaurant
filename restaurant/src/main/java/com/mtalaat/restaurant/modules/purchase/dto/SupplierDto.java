package com.mtalaat.restaurant.modules.purchase.dto;

import com.mtalaat.restaurant.modules.account.dto.AccountDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;
    private String email;
    private String phone;
    private String address;
    private Boolean status;
    private Long accountId;
    private String accountCode;
    private String accountName;
    private BigDecimal accountBalance;
}
