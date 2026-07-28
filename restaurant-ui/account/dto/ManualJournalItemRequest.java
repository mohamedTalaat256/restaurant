package com.mtalaat.restaurant.modules.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManualJournalItemRequest {

    @NotNull(message = "validation_account_id_required")
    private Long accountId;

    @NotNull(message = "validation_debit_required")
    @DecimalMin(value = "0.0", message = "validation_debit_non_negative")
    private BigDecimal debit;

    @NotNull(message = "validation_credit_required")
    @DecimalMin(value = "0.0", message = "validation_credit_non_negative")
    private BigDecimal credit;

    private Long costCenterId;
}
