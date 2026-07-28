package com.mtalaat.restaurant.modules.settings.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.settings.dto.ThirdPartyCustomerDto;
import com.mtalaat.restaurant.modules.settings.entity.ThirdPartyCustomer;
import com.mtalaat.restaurant.modules.settings.mapping.ThirdPartyCustomerMapper;
import com.mtalaat.restaurant.modules.settings.repository.ThirdPartyCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThirdPartyCustomerService {

    private final ThirdPartyCustomerRepository thirdPartyCustomerRepository;
    private final ThirdPartyCustomerMapper thirdPartyCustomerMapper;

    public ThirdPartyCustomerDto add(ThirdPartyCustomerDto dto) {
        return create(dto);
    }

    public ThirdPartyCustomerDto create(ThirdPartyCustomerDto dto) {
        ThirdPartyCustomer entity = thirdPartyCustomerMapper.toEntity(dto);
        return thirdPartyCustomerMapper.toDto(thirdPartyCustomerRepository.save(entity));
    }

    public ThirdPartyCustomerDto getById(Long id) {
        ThirdPartyCustomer entity = thirdPartyCustomerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Third party customer not found with id: " + id));
        return thirdPartyCustomerMapper.toDto(entity);
    }

    public List<ThirdPartyCustomerDto> getAll() {
        return thirdPartyCustomerRepository.findAll().stream().map(thirdPartyCustomerMapper::toDto).toList();
    }

    public ThirdPartyCustomerDto update(Long id, ThirdPartyCustomerDto dto) {
        ThirdPartyCustomer entity = thirdPartyCustomerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Third party customer not found with id: " + id));
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setCommissionPercentage(dto.getCommissionPercentage());
        return thirdPartyCustomerMapper.toDto(thirdPartyCustomerRepository.save(entity));
    }

    public void delete(Long id) {
        ThirdPartyCustomer entity = thirdPartyCustomerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Third party customer not found with id: " + id));
        thirdPartyCustomerRepository.delete(entity);
    }
}
