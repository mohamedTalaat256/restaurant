package com.mtalaat.restaurant.modules.settings.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.settings.dto.UnitOfMeasurementDto;
import com.mtalaat.restaurant.modules.settings.entity.UnitOfMeasurement;
import com.mtalaat.restaurant.modules.settings.mapping.UnitOfMeasurementMapper;
import com.mtalaat.restaurant.modules.settings.repository.UnitOfMeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitOfMeasurementService {

    private final UnitOfMeasurementRepository unitOfMeasurementRepository;
    private final UnitOfMeasurementMapper unitOfMeasurementMapper;

    public UnitOfMeasurementDto add(UnitOfMeasurementDto dto) {
        return create(dto);
    }

    public UnitOfMeasurementDto create(UnitOfMeasurementDto dto) {
        UnitOfMeasurement entity = unitOfMeasurementMapper.toEntity(dto);
        return unitOfMeasurementMapper.toDto(unitOfMeasurementRepository.save(entity));
    }

    public UnitOfMeasurementDto getById(Long id) {
        UnitOfMeasurement entity = unitOfMeasurementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit of measurement not found with id: " + id));
        return unitOfMeasurementMapper.toDto(entity);
    }

    public List<UnitOfMeasurementDto> getAll() {
        return unitOfMeasurementRepository.findAll().stream().map(unitOfMeasurementMapper::toDto).toList();
    }

    public UnitOfMeasurementDto update(Long id, UnitOfMeasurementDto dto) {
        UnitOfMeasurement entity = unitOfMeasurementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit of measurement not found with id: " + id));

        entity.setName(dto.getName());
        entity.setShortName(dto.getShortName());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());

        return unitOfMeasurementMapper.toDto(unitOfMeasurementRepository.save(entity));
    }

    public void delete(Long id) {
        UnitOfMeasurement entity = unitOfMeasurementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit of measurement not found with id: " + id));
        unitOfMeasurementRepository.delete(entity);
    }
}
