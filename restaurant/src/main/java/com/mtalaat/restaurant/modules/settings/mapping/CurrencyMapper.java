package com.mtalaat.restaurant.modules.settings.mapping;

import com.mtalaat.restaurant.modules.settings.dto.CurrencyDto;
import com.mtalaat.restaurant.modules.settings.entity.Currency;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {

    public CurrencyDto toDto(Currency entity) {
        return CurrencyDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .symbol(entity.getSymbol())
                .name(entity.getName())
                .exchangeRateToUSD(entity.getExchangeRateToUSD())
                .build();
    }

    public Currency toEntity(CurrencyDto dto) {
        return Currency.builder()
                .code(dto.getCode())
                .symbol(dto.getSymbol())
                .name(dto.getName())
                .exchangeRateToUSD(dto.getExchangeRateToUSD())
                .build();
    }
}
