package com.mtalaat.restaurant.modules.account.dto;

import com.mtalaat.restaurant.modules.account.enums.AccountType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrialBalanceLineDTO {

    private Long accountId;
    private String accountCode;
    private String accountName;
    private AccountType accountType;
    private BigDecimal openingBalance;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private BigDecimal closingBalance;
}
