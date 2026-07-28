package com.mtalaat.restaurant.modules.order.entity;

import com.mtalaat.restaurant.modules.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cash_registers")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_counter_id", nullable = false)
    private CashCounter cashCounter;

    @Column(name = "opening_balance", nullable = false)
    private Double openingBalance;

    @Column(name = "closing_balance")
    private Double closingBalance;

    @Column(name = "opening_time", nullable = false)
    private LocalDateTime openingTime;

    @Column(name = "closing_time")
    private LocalDateTime closingTime;

    @Column(name = "opening_note")
    private String openingNote;

    @Column(name = "closing_note")
    private String closingNote;

    @Builder.Default
    @Column(name = "status", nullable = false)
    private Boolean status = true;
}
