package com.mtalaat.restaurant.modules.foodManagement.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodAddOnsDto;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFoodAddOns;
import com.mtalaat.restaurant.modules.foodManagement.mapping.ItemFoodAddOnsMapper;
import com.mtalaat.restaurant.modules.foodManagement.repository.ItemFoodAddOnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemFoodAddOnsService {

    private final ItemFoodAddOnsRepository itemFoodAddOnsRepository;
    private final ItemFoodAddOnsMapper itemFoodAddOnsMapper;

    public ItemFoodAddOnsDto add(ItemFoodAddOnsDto dto) {
        return create(dto);
    }

    public ItemFoodAddOnsDto create(ItemFoodAddOnsDto dto) {
        ItemFoodAddOns entity = itemFoodAddOnsMapper.toEntity(dto);
        return itemFoodAddOnsMapper.toDto(itemFoodAddOnsRepository.save(entity));
    }

    public ItemFoodAddOnsDto getById(Long id) {
        ItemFoodAddOns entity = itemFoodAddOnsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFoodAddOns not found with id: " + id));
        return itemFoodAddOnsMapper.toDto(entity);
    }

    public List<ItemFoodAddOnsDto> getAll() {
        return itemFoodAddOnsRepository.findAll().stream().map(itemFoodAddOnsMapper::toDto).toList();
    }

    public ItemFoodAddOnsDto update(Long id, ItemFoodAddOnsDto dto) {
        ItemFoodAddOns entity = itemFoodAddOnsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFoodAddOns not found with id: " + id));

        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());

        return itemFoodAddOnsMapper.toDto(itemFoodAddOnsRepository.save(entity));
    }

    public void delete(Long id) {
        ItemFoodAddOns entity = itemFoodAddOnsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFoodAddOns not found with id: " + id));
        itemFoodAddOnsRepository.delete(entity);
    }
}
