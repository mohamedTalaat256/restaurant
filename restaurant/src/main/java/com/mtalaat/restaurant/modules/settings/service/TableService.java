package com.mtalaat.restaurant.modules.settings.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.settings.dto.TableDto;
import com.mtalaat.restaurant.modules.settings.entity.Floor;
import com.mtalaat.restaurant.modules.settings.entity.RestaurantTable;
import com.mtalaat.restaurant.modules.settings.mapping.TableMapper;
import com.mtalaat.restaurant.modules.settings.repository.FloorRepository;
import com.mtalaat.restaurant.modules.settings.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final FloorRepository floorRepository;
    private final TableMapper tableMapper;

    public TableDto add(TableDto dto) {
        return create(dto);
    }

    public TableDto create(TableDto dto) {
        Floor floor = floorRepository.findById(dto.getFloorId())
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + dto.getFloorId()));
        RestaurantTable entity = tableMapper.toEntity(dto, floor);
        return tableMapper.toDto(tableRepository.save(entity));
    }

    public TableDto getById(Long id) {
        RestaurantTable entity = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
        return tableMapper.toDto(entity);
    }

    public List<TableDto> getAll() {
        return tableRepository.findAll().stream().map(tableMapper::toDto).toList();
    }

    public TableDto update(Long id, TableDto dto) {
        RestaurantTable entity = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));

        Floor floor = floorRepository.findById(dto.getFloorId())
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + dto.getFloorId()));

        entity.setName(dto.getName());
        entity.setCapacity(dto.getCapacity());
        entity.setIcon(dto.getIcon());
        entity.setFloor(floor);
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());

        return tableMapper.toDto(tableRepository.save(entity));
    }

    public void delete(Long id) {
        RestaurantTable entity = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
        tableRepository.delete(entity);
    }
}
