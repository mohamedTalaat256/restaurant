package com.mtalaat.restaurant.modules.foodManagement.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodAddOnsAssociationDto;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFood;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFoodAddOns;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFoodAddOnsAssociation;
import com.mtalaat.restaurant.modules.foodManagement.mapping.ItemFoodAddOnsAssociationMapper;
import com.mtalaat.restaurant.modules.foodManagement.repository.ItemFoodAddOnsAssociationRepository;
import com.mtalaat.restaurant.modules.foodManagement.repository.ItemFoodAddOnsRepository;
import com.mtalaat.restaurant.modules.foodManagement.repository.ItemFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemFoodAddOnsAssociationService {

    private final ItemFoodAddOnsAssociationRepository itemFoodAddOnsAssociationRepository;
    private final ItemFoodRepository itemFoodRepository;
    private final ItemFoodAddOnsRepository itemFoodAddOnsRepository;
    private final ItemFoodAddOnsAssociationMapper itemFoodAddOnsAssociationMapper;

    public ItemFoodAddOnsAssociationDto add(ItemFoodAddOnsAssociationDto dto) {
        return create(dto);
    }

    public ItemFoodAddOnsAssociationDto create(ItemFoodAddOnsAssociationDto dto) {
        ItemFood itemFood = resolveItemFood(dto.getItemFoodId());
        ItemFoodAddOns itemFoodAddOns = resolveItemFoodAddOns(dto.getItemFoodAddOnsId());
        ItemFoodAddOnsAssociation entity = itemFoodAddOnsAssociationMapper.toEntity(dto, itemFood, itemFoodAddOns);
        return itemFoodAddOnsAssociationMapper.toDto(itemFoodAddOnsAssociationRepository.save(entity));
    }

    public ItemFoodAddOnsAssociationDto getById(Long id) {
        ItemFoodAddOnsAssociation entity = itemFoodAddOnsAssociationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFoodAddOnsAssociation not found with id: " + id));
        return itemFoodAddOnsAssociationMapper.toDto(entity);
    }

    public List<ItemFoodAddOnsAssociationDto> getAll() {
        return itemFoodAddOnsAssociationRepository.findAll().stream().map(itemFoodAddOnsAssociationMapper::toDto).toList();
    }

    public ItemFoodAddOnsAssociationDto update(Long id, ItemFoodAddOnsAssociationDto dto) {
        ItemFoodAddOnsAssociation entity = itemFoodAddOnsAssociationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFoodAddOnsAssociation not found with id: " + id));

        ItemFood itemFood = resolveItemFood(dto.getItemFoodId());
        ItemFoodAddOns itemFoodAddOns = resolveItemFoodAddOns(dto.getItemFoodAddOnsId());

        entity.setItemFood(itemFood);
        entity.setItemFoodAddOns(itemFoodAddOns);

        return itemFoodAddOnsAssociationMapper.toDto(itemFoodAddOnsAssociationRepository.save(entity));
    }

    public void delete(Long id) {
        ItemFoodAddOnsAssociation entity = itemFoodAddOnsAssociationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFoodAddOnsAssociation not found with id: " + id));
        itemFoodAddOnsAssociationRepository.delete(entity);
    }

    private ItemFood resolveItemFood(Long itemFoodId) {
        if (itemFoodId == null) return null;
        return itemFoodRepository.findById(itemFoodId)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFood not found with id: " + itemFoodId));
    }

    private ItemFoodAddOns resolveItemFoodAddOns(Long itemFoodAddOnsId) {
        if (itemFoodAddOnsId == null) return null;
        return itemFoodAddOnsRepository.findById(itemFoodAddOnsId)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFoodAddOns not found with id: " + itemFoodAddOnsId));
    }
}
