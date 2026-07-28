package com.mtalaat.restaurant.modules.account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceSheetSectionDTO {

    private String sectionName;
    private List<BalanceSheetLineDTO> accounts;
    private BigDecimal sectionTotal;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BalanceSheetLineDTO {
        private Long accountId;
        private String accountCode;
        private String accountName;
        private BigDecimal balance;
    }
}
