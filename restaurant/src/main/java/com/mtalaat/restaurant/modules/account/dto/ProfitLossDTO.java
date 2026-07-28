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
public class ProfitLossDTO {

    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    private List<ProfitLossLineDTO> revenueAccounts;
    private BigDecimal totalRevenue;

    private List<ProfitLossLineDTO> cogsAccounts;
    private BigDecimal totalCogs;

    private BigDecimal grossProfit;

    private List<ProfitLossLineDTO> operatingExpenseAccounts;
    private BigDecimal totalOperatingExpenses;

    private BigDecimal netProfit;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProfitLossLineDTO {
        private Long accountId;
        private String accountCode;
        private String accountName;
        private BigDecimal amount;
    }
}
