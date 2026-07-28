package com.mtalaat.restaurant.modules.settings.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.settings.dto.KitchenDto;
import com.mtalaat.restaurant.modules.settings.entity.Kitchen;
import com.mtalaat.restaurant.modules.settings.mapping.KitchenMapper;
import com.mtalaat.restaurant.modules.settings.repository.KitchenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KitchenService {

    private final KitchenRepository kitchenRepository;
    private final KitchenMapper kitchenMapper;

    public KitchenDto add(KitchenDto dto) {
        return create(dto);
    }

    public KitchenDto create(KitchenDto dto) {
        Kitchen entity = kitchenMapper.toEntity(dto);
        return kitchenMapper.toDto(kitchenRepository.save(entity));
    }

    public KitchenDto getById(Long id) {
        Kitchen entity = kitchenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitchen not found with id: " + id));
        return kitchenMapper.toDto(entity);
    }

    public List<KitchenDto> getAll() {
        return kitchenRepository.findAll().stream().map(kitchenMapper::toDto).toList();
    }

    public KitchenDto update(Long id, KitchenDto dto) {
        Kitchen entity = kitchenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitchen not found with id: " + id));

        entity.setName(dto.getName());
        entity.setIpAddress(dto.getIpAddress());
        entity.setPort(dto.getPort());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());

        return kitchenMapper.toDto(kitchenRepository.save(entity));
    }

    public void delete(Long id) {
        Kitchen entity = kitchenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitchen not found with id: " + id));
        kitchenRepository.delete(entity);
    }
}
