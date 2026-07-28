package com.mtalaat.restaurant.modules.account.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JournalItemDTO {
    private Long accountId;
    private String accountName;
    private String accountCode;
    private BigDecimal debit;
    private BigDecimal credit;
    private Long costCenterId;
    private String costCenterName;
}