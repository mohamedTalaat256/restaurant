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
public class GeneralLedgerReportDTO {

    private Long accountId;
    private String accountCode;
    private String accountName;
    private String accountType;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private BigDecimal openingBalance;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private BigDecimal closingBalance;
    private List<GeneralLedgerLineDTO> lines;
}
