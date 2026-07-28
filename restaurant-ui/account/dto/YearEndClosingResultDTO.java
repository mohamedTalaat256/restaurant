package com.mtalaat.restaurant.modules.account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YearEndClosingResultDTO {

    private Integer fiscalYear;
    private LocalDateTime closingDate;
    private String closingEntryNumber;
    private BigDecimal totalRevenue;
    private BigDecimal totalExpenses;
    private BigDecimal netIncome;
    private int accountsClosed;
}
