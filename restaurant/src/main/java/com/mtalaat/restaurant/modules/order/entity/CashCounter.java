package com.mtalaat.restaurant.modules.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cash_counters")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false)
    private Integer number;
}
