package com.mtalaat.restaurant.modules.settings.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.settings.dto.CurrencyDto;
import com.mtalaat.restaurant.modules.settings.entity.Currency;
import com.mtalaat.restaurant.modules.settings.mapping.CurrencyMapper;
import com.mtalaat.restaurant.modules.settings.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    public CurrencyDto add(CurrencyDto dto) {
        return create(dto);
    }

    public CurrencyDto create(CurrencyDto dto) {
        Currency entity = currencyMapper.toEntity(dto);
        return currencyMapper.toDto(currencyRepository.save(entity));
    }

    public CurrencyDto getById(Long id) {
        Currency entity = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));
        return currencyMapper.toDto(entity);
    }

    public List<CurrencyDto> getAll() {
        return currencyRepository.findAll().stream().map(currencyMapper::toDto).toList();
    }

    public CurrencyDto update(Long id, CurrencyDto dto) {
        Currency entity = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));
        entity.setCode(dto.getCode());
        entity.setSymbol(dto.getSymbol());
        entity.setName(dto.getName());
        entity.setExchangeRateToUSD(dto.getExchangeRateToUSD());
        return currencyMapper.toDto(currencyRepository.save(entity));
    }

    public void delete(Long id) {
        Currency entity = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));
        currencyRepository.delete(entity);
    }
}
