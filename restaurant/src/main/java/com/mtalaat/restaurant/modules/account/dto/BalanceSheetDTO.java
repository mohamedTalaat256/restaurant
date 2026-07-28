package com.mtalaat.restaurant.modules.account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceSheetDTO {

    private LocalDateTime asOfDate;
    private BalanceSheetSectionDTO assets;
    private BalanceSheetSectionDTO liabilities;
    private BalanceSheetSectionDTO equity;
    private BigDecimal totalLiabilitiesAndEquity;
    private boolean isBalanced;
}
