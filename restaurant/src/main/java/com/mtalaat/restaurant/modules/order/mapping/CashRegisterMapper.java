package com.mtalaat.restaurant.modules.order.mapping;

import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.order.dto.CashRegisterDto;
import com.mtalaat.restaurant.modules.order.dto.OpenCashRegisterDto;
import com.mtalaat.restaurant.modules.order.entity.CashCounter;
import com.mtalaat.restaurant.modules.order.entity.CashRegister;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CashRegisterMapper {

    public CashRegisterDto toDto(CashRegister entity) {
        return CashRegisterDto.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .userName(entity.getUser() != null ? entity.getUser().getFirstname() + " " + entity.getUser().getLastname() : null)
                .cashCounterId(entity.getCashCounter() != null ? entity.getCashCounter().getId() : null)
                .cashCounterNumber(entity.getCashCounter() != null ? entity.getCashCounter().getNumber() : null)
                .openingBalance(entity.getOpeningBalance())
                .closingBalance(entity.getClosingBalance())
                .openingTime(entity.getOpeningTime())
                .closingTime(entity.getClosingTime())
                .openingNote(entity.getOpeningNote())
                .closingNote(entity.getClosingNote())
                .status(entity.getStatus())
                .build();
    }

    public CashRegister toEntity(OpenCashRegisterDto dto, User user, CashCounter cashCounter) {
        return CashRegister.builder()
                .user(user)
                .cashCounter(cashCounter)
                .openingBalance(dto.getOpeningBalance())
                .openingTime(LocalDateTime.now())
                .openingNote(dto.getOpeningNote())
                .status(true)
                .build();
    }
}
