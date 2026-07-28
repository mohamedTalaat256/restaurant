package com.mtalaat.restaurant.modules.account.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiscalPeriodDTO {

    private Long id;
    private Integer year;
    private Integer month;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean locked;
    private LocalDateTime lockedAt;
    private String lockedByUsername;
}
