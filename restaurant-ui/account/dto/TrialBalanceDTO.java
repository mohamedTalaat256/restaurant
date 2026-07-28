package com.mtalaat.restaurant.modules.account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrialBalanceDTO {

    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private List<TrialBalanceLineDTO> lines;
    private BigDecimal totalOpeningDebit;
    private BigDecimal totalOpeningCredit;
    private BigDecimal totalPeriodDebit;
    private BigDecimal totalPeriodCredit;
    private BigDecimal totalClosingDebit;
    private BigDecimal totalClosingCredit;
}
