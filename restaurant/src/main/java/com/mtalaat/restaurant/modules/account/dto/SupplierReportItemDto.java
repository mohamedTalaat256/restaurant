package com.mtalaat.restaurant.modules.account.dto;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor // سينشئ واحد لجميع الحقول الـ 7
@NoArgsConstructor
@Builder
public class SupplierReportItemDto {

    private String entryNumber;
    private LocalDateTime date;
    private String description;
    private String reference;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal runningBalance; // سيتم تعيينه لاحقاً في الـ Service

     public SupplierReportItemDto(String entryNumber, LocalDateTime date, String description,
                                 String reference, BigDecimal debit, BigDecimal credit) {
        this.entryNumber = entryNumber;
        this.date = date;
        this.description = description;
        this.reference = reference;
        this.debit = debit;
        this.credit = credit;
        this.runningBalance = BigDecimal.ZERO;
    }
}