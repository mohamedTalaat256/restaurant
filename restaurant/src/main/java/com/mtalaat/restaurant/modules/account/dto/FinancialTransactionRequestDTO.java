package com.mtalaat.restaurant.modules.account.dto;

import com.mtalaat.restaurant.modules.account.enums.FinancialTransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinancialTransactionRequestDTO {
    private FinancialTransactionType transactionType;
    private Long sourceAccountId;       // الحساب المأخوذ منه الفلوس (المعطي / الدائن دائمًا)
    private Long destinationAccountId;  // الحساب اللي الفلوس رايحة له (الآخذ / المدين دائمًا)
    private BigDecimal amount;
    private String description;
    private String reference;
    private Long costCenterId;
}
