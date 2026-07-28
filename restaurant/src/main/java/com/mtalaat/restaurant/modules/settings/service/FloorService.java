package com.mtalaat.restaurant.modules.settings.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.settings.dto.FloorDto;
import com.mtalaat.restaurant.modules.settings.entity.Floor;
import com.mtalaat.restaurant.modules.settings.mapping.FloorMapper;
import com.mtalaat.restaurant.modules.settings.repository.FloorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FloorService {

    private final FloorRepository floorRepository;
    private final FloorMapper floorMapper;

    public FloorDto add(FloorDto dto) {
        return create(dto);
    }

    public FloorDto create(FloorDto dto) {
        Floor entity = floorMapper.toEntity(dto);
        return floorMapper.toDto(floorRepository.save(entity));
    }

    public FloorDto getById(Long id) {
        Floor entity = floorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + id));
        return floorMapper.toDto(entity);
    }

    public List<FloorDto> getAll() {
        return floorRepository.findAll().stream().map(floorMapper::toDto).toList();
    }

    public FloorDto update(Long id, FloorDto dto) {
        Floor entity = floorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + id));

        entity.setName(dto.getName());

        return floorMapper.toDto(floorRepository.save(entity));
    }

    public void delete(Long id) {
        Floor entity = floorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + id));
        floorRepository.delete(entity);
    }
}
