package com.mtalaat.restaurant.modules.account.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "acc_fiscal_periods",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fiscal_year", "fiscal_month"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiscalPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fiscal_year", nullable = false)
    private Integer year;

    @Column(name = "fiscal_month", nullable = false)
    private Integer month;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Builder.Default
    @Column(nullable = false)
    private Boolean locked = false;

    private LocalDateTime lockedAt;

    private String lockedByUsername;
}
