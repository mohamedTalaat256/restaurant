package com.mtalaat.restaurant.modules.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashRegisterDto {

    private Long id;
    private Long userId;
    private String userName;
    private Long cashCounterId;
    private Integer cashCounterNumber;
    private Double openingBalance;
    private Double closingBalance;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private String openingNote;
    private String closingNote;
    private Boolean status;
}
