package com.mtalaat.restaurant.modules.foodManagement.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodVariantDto;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFood;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFoodVariant;
import com.mtalaat.restaurant.modules.foodManagement.mapping.ItemFoodVariantMapper;
import com.mtalaat.restaurant.modules.foodManagement.repository.ItemFoodRepository;
import com.mtalaat.restaurant.modules.foodManagement.repository.ItemFoodVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemFoodVariantService {

    private final ItemFoodVariantRepository itemFoodVariantRepository;
    private final ItemFoodRepository itemFoodRepository;
    private final ItemFoodVariantMapper itemFoodVariantMapper;

    public ItemFoodVariantDto add(ItemFoodVariantDto dto) {
        return create(dto);
    }

    public ItemFoodVariantDto create(ItemFoodVariantDto dto) {
        ItemFood itemFood = resolveItemFood(dto.getItemFoodId());
        ItemFoodVariant entity = itemFoodVariantMapper.toEntity(dto, itemFood);
        return itemFoodVariantMapper.toDto(itemFoodVariantRepository.save(entity));
    }

    public ItemFoodVariantDto getById(Long id) {
        ItemFoodVariant entity = itemFoodVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFoodVariant not found with id: " + id));
        return itemFoodVariantMapper.toDto(entity);
    }

    public List<ItemFoodVariantDto> getAll() {
        return itemFoodVariantRepository.findAll().stream().map(itemFoodVariantMapper::toDto).toList();
    }

    public ItemFoodVariantDto update(Long id, ItemFoodVariantDto dto) {
        ItemFoodVariant entity = itemFoodVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFoodVariant not found with id: " + id));

        ItemFood itemFood = resolveItemFood(dto.getItemFoodId());

        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setItemFood(itemFood);

        return itemFoodVariantMapper.toDto(itemFoodVariantRepository.save(entity));
    }

    public void delete(Long id) {
        ItemFoodVariant entity = itemFoodVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFoodVariant not found with id: " + id));
        itemFoodVariantRepository.delete(entity);
    }

    private ItemFood resolveItemFood(Long itemFoodId) {
        if (itemFoodId == null) return null;
        return itemFoodRepository.findById(itemFoodId)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFood not found with id: " + itemFoodId));
    }
}
