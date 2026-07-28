package com.mtalaat.restaurant.modules.order.service;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.order.dto.CashRegisterDto;
import com.mtalaat.restaurant.modules.order.dto.CloseCashRegisterDto;
import com.mtalaat.restaurant.modules.order.dto.OpenCashRegisterDto;
import com.mtalaat.restaurant.modules.order.entity.CashCounter;
import com.mtalaat.restaurant.modules.order.entity.CashRegister;
import com.mtalaat.restaurant.modules.order.mapping.CashRegisterMapper;
import com.mtalaat.restaurant.modules.order.repository.CashCounterRepository;
import com.mtalaat.restaurant.modules.order.repository.CashRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CashRegisterService {

    private final CashRegisterRepository cashRegisterRepository;
    private final CashCounterRepository cashCounterRepository;
    private final CashRegisterMapper cashRegisterMapper;

    public CashRegisterDto getById(Long id) {
        CashRegister entity = cashRegisterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register not found with id: " + id));
        return cashRegisterMapper.toDto(entity);
    }

    public List<CashRegisterDto> getAll() {
        return cashRegisterRepository.findAll().stream().map(cashRegisterMapper::toDto).toList();
    }

    public void delete(Long id) {
        CashRegister entity = cashRegisterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register not found with id: " + id));
        cashRegisterRepository.delete(entity);
    }

    public Optional<CashRegisterDto> getOpenCashRegister(User user) {
        return cashRegisterRepository.findByUserAndStatus(user, true)
                .map(cashRegisterMapper::toDto);
    }

    public CashRegisterDto openCashRegister(OpenCashRegisterDto dto, User user) {
        Optional<CashRegister> existingOpen = cashRegisterRepository.findByUserAndStatus(user, true);
        if (existingOpen.isPresent()) {
            throw new BadRequestException("msg_cash_register_already_open");
        }

        CashCounter cashCounter = cashCounterRepository.findById(dto.getCashCounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Cash counter not found with id: " + dto.getCashCounterId()));

        CashRegister entity = cashRegisterMapper.toEntity(dto, user, cashCounter);
        return cashRegisterMapper.toDto(cashRegisterRepository.save(entity));
    }

    public CashRegisterDto closeCashRegister(CloseCashRegisterDto dto, User user) {
        CashRegister entity = cashRegisterRepository.findByUserAndStatus(user, true)
                .orElseThrow(() -> new BadRequestException("msg_no_open_cash_register"));

        entity.setClosingBalance(dto.getClosingBalance());
        entity.setClosingNote(dto.getClosingNote());
        entity.setClosingTime(LocalDateTime.now());
        entity.setStatus(false);

        return cashRegisterMapper.toDto(cashRegisterRepository.save(entity));
    }
}
