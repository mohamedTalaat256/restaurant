package com.mtalaat.restaurant.modules.order.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.order.dto.CashCounterDto;
import com.mtalaat.restaurant.modules.order.entity.CashCounter;
import com.mtalaat.restaurant.modules.order.mapping.CashCounterMapper;
import com.mtalaat.restaurant.modules.order.repository.CashCounterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CashCounterService {

    private final CashCounterRepository cashCounterRepository;
    private final CashCounterMapper cashCounterMapper;

    public CashCounterDto add(CashCounterDto dto) {
        return create(dto);
    }

    public CashCounterDto create(CashCounterDto dto) {
        CashCounter entity = cashCounterMapper.toEntity(dto);
        return cashCounterMapper.toDto(cashCounterRepository.save(entity));
    }

    public CashCounterDto getById(Long id) {
        CashCounter entity = cashCounterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cash counter not found with id: " + id));
        return cashCounterMapper.toDto(entity);
    }

    public List<CashCounterDto> getAll() {
        return cashCounterRepository.findAll().stream().map(cashCounterMapper::toDto).toList();
    }

    public CashCounterDto update(Long id, CashCounterDto dto) {
        CashCounter entity = cashCounterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cash counter not found with id: " + id));
        entity.setNumber(dto.getNumber());
        return cashCounterMapper.toDto(cashCounterRepository.save(entity));
    }

    public void delete(Long id) {
        CashCounter entity = cashCounterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cash counter not found with id: " + id));
        cashCounterRepository.delete(entity);
    }
}
