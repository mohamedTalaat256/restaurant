package com.mtalaat.restaurant.modules.account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralLedgerLineDTO {

    private LocalDateTime date;
    private String entryNumber;
    private String description;
    private String reference;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal runningBalance;
    private String costCenterName;
}
