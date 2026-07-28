package com.mtalaat.restaurant.modules.account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierStatementReport {

    private Long supplierAccountId;
    private String supplierName;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private BigDecimal openingBalance; // الرصيد الافتتاحي قبل الفترة
    private List<SupplierReportItemDto> items;
    private BigDecimal closingBalance;
}
