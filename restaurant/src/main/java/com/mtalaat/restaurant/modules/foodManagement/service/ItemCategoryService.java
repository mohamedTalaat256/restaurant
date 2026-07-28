package com.mtalaat.restaurant.modules.foodManagement.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.foodManagement.dto.ItemCategoryDto;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemCategory;
import com.mtalaat.restaurant.modules.foodManagement.mapping.ItemCategoryMapper;
import com.mtalaat.restaurant.modules.foodManagement.repository.ItemCategoryRepository;
import com.mtalaat.restaurant.utility.FileUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemCategoryMapper itemCategoryMapper;
    private final FileUtility fileUtility;

    public ItemCategoryDto add(ItemCategoryDto dto, MultipartFile image) {
        return create(dto, image);
    }

    public ItemCategoryDto create(ItemCategoryDto dto, MultipartFile image) {
        ItemCategory entity = itemCategoryMapper.toEntity(dto);

        if (image != null && !image.isEmpty()) {
            String imagePath = fileUtility.saveFile(image, "food");
            entity.setImage(imagePath);
        }

        return itemCategoryMapper.toDto(itemCategoryRepository.save(entity));
    }

    public ItemCategoryDto getById(Long id) {
        ItemCategory entity = itemCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemCategory not found with id: " + id));
        return itemCategoryMapper.toDto(entity);
    }

    public List<ItemCategoryDto> getAll() {
        return itemCategoryRepository.findAll().stream().map(itemCategoryMapper::toDto).toList();
    }

    public ItemCategoryDto update(Long id, ItemCategoryDto dto, MultipartFile image) {
        ItemCategory entity = itemCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemCategory not found with id: " + id));

        entity.setName(dto.getName());
        entity.setPosition(dto.getPosition());
        entity.setIsOffer(dto.getIsOffer() == null ? Boolean.FALSE : dto.getIsOffer());
        entity.setOfferStartDate(dto.getOfferStartDate());
        entity.setOfferEndDate(dto.getOfferEndDate());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());

        if (image != null && !image.isEmpty()) {
            String imagePath = fileUtility.saveFile(image, "food");
            entity.setImage(imagePath);
        }

        return itemCategoryMapper.toDto(itemCategoryRepository.save(entity));
    }

    public void delete(Long id) {
        ItemCategory entity = itemCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemCategory not found with id: " + id));
        itemCategoryRepository.delete(entity);
    }
}
