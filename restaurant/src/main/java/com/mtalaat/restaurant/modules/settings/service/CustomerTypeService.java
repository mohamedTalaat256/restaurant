package com.mtalaat.restaurant.modules.settings.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.settings.dto.CustomerTypeDto;
import com.mtalaat.restaurant.modules.settings.entity.CustomerType;
import com.mtalaat.restaurant.modules.settings.mapping.CustomerTypeMapper;
import com.mtalaat.restaurant.modules.settings.repository.CustomerTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerTypeService {

    private final CustomerTypeRepository customerTypeRepository;
    private final CustomerTypeMapper customerTypeMapper;

    public CustomerTypeDto add(CustomerTypeDto dto) {
        return create(dto);
    }

    public CustomerTypeDto create(CustomerTypeDto dto) {
        CustomerType entity = customerTypeMapper.toEntity(dto);
        return customerTypeMapper.toDto(customerTypeRepository.save(entity));
    }

    public CustomerTypeDto getById(String type) {
        CustomerType entity = customerTypeRepository.findById(type)
                .orElseThrow(() -> new ResourceNotFoundException("Customer type not found with type: " + type));
        return customerTypeMapper.toDto(entity);
    }

    public List<CustomerTypeDto> getAll() {
        return customerTypeRepository.findAll().stream().map(customerTypeMapper::toDto).toList();
    }

    public CustomerTypeDto update(String type, CustomerTypeDto dto) {
        CustomerType entity = customerTypeRepository.findById(type)
                .orElseThrow(() -> new ResourceNotFoundException("Customer type not found with type: " + type));
        entity.setDescription(dto.getDescription());
        entity.setOrdering(dto.getOrdering());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());
        return customerTypeMapper.toDto(customerTypeRepository.save(entity));
    }

    public void delete(String type) {
        CustomerType entity = customerTypeRepository.findById(type)
                .orElseThrow(() -> new ResourceNotFoundException("Customer type not found with type: " + type));
        customerTypeRepository.delete(entity);
    }
}
