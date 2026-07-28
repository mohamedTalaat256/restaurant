package com.mtalaat.restaurant.modules.settings.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "currencies")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "exchange_rate_to_usd", nullable = false)
    private Double exchangeRateToUSD;
}
