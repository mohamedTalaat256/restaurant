package com.mtalaat.restaurant.modules.foodManagement.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodDto;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemCategory;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFood;
import com.mtalaat.restaurant.modules.foodManagement.entity.MenuType;
import com.mtalaat.restaurant.modules.foodManagement.mapping.ItemFoodMapper;
import com.mtalaat.restaurant.modules.foodManagement.repository.ItemCategoryRepository;
import com.mtalaat.restaurant.modules.foodManagement.repository.ItemFoodRepository;
import com.mtalaat.restaurant.modules.foodManagement.repository.MenuTypeRepository;
import com.mtalaat.restaurant.modules.settings.entity.Kitchen;
import com.mtalaat.restaurant.modules.settings.repository.KitchenRepository;
import com.mtalaat.restaurant.utility.FileUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemFoodService {

    private final ItemFoodRepository itemFoodRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final KitchenRepository kitchenRepository;
    private final MenuTypeRepository menuTypeRepository;
    private final ItemFoodMapper itemFoodMapper;
    private final FileUtility fileUtility;

    public ItemFoodDto add(ItemFoodDto dto, MultipartFile image) {
        return create(dto, image);
    }

    public ItemFoodDto create(ItemFoodDto dto, MultipartFile image) {
        ItemCategory category = resolveCategory(dto.getCategoryId());
        Kitchen kitchen = resolveKitchen(dto.getKitchenId());
        MenuType menuType = resolveMenuType(dto.getMenuTypeId());

        ItemFood entity = itemFoodMapper.toEntity(dto, category, kitchen, menuType);

        if (image != null && !image.isEmpty()) {
            String imagePath = fileUtility.saveFile(image, "food");
            entity.setImage(imagePath);
        }

        return itemFoodMapper.toDto(itemFoodRepository.save(entity));
    }

    public ItemFoodDto getById(Long id) {
        ItemFood entity = itemFoodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFood not found with id: " + id));
        return itemFoodMapper.toDto(entity);
    }

    public List<ItemFoodDto> getAll() {
        return itemFoodRepository.findAll().stream().map(itemFoodMapper::toDto).toList();
    }

    public ItemFoodDto update(Long id, ItemFoodDto dto, MultipartFile image) {
        ItemFood entity = itemFoodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFood not found with id: " + id));

        ItemCategory category = resolveCategory(dto.getCategoryId());
        Kitchen kitchen = resolveKitchen(dto.getKitchenId());
        MenuType menuType = resolveMenuType(dto.getMenuTypeId());

        entity.setName(dto.getName());
        entity.setDescrip(dto.getDescrip());
        entity.setComponent(dto.getComponent());
        entity.setNote(dto.getNote());
        entity.setPosition(dto.getPosition());
        entity.setIsGroup(dto.getIsGroup() == null ? Boolean.FALSE : dto.getIsGroup());
        entity.setCookedTime(dto.getCookedTime());
        entity.setOfferIsAvailable(dto.getOfferIsAvailable() == null ? Boolean.FALSE : dto.getOfferIsAvailable());
        entity.setOfferRate(dto.getOfferRate());
        entity.setOfferStartDate(dto.getOfferStartDate());
        entity.setOfferEndDate(dto.getOfferEndDate());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());
        entity.setCategory(category);
        entity.setKitchen(kitchen);
        entity.setIsCustomQty(dto.getIsCustomQty() == null ? Boolean.FALSE : dto.getIsCustomQty());
        entity.setIsSpecial(dto.getIsSpecial() == null ? Boolean.FALSE : dto.getIsSpecial());
        entity.setProductVat(dto.getProductVat());
        entity.setTax0(dto.getTax0());
        entity.setTax1(dto.getTax1());
        entity.setMenuType(menuType);

        if (image != null && !image.isEmpty()) {
            String imagePath = fileUtility.saveFile(image, "food");
            entity.setImage(imagePath);
        }

        return itemFoodMapper.toDto(itemFoodRepository.save(entity));
    }

    public void delete(Long id) {
        ItemFood entity = itemFoodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemFood not found with id: " + id));
        itemFoodRepository.delete(entity);
    }

    private ItemCategory resolveCategory(Long categoryId) {
        if (categoryId == null) return null;
        return itemCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("ItemCategory not found with id: " + categoryId));
    }

    private Kitchen resolveKitchen(Long kitchenId) {
        if (kitchenId == null) return null;
        return kitchenRepository.findById(kitchenId)
                .orElseThrow(() -> new ResourceNotFoundException("Kitchen not found with id: " + kitchenId));
    }

    private MenuType resolveMenuType(Long menuTypeId) {
        if (menuTypeId == null) return null;
        return menuTypeRepository.findById(menuTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("MenuType not found with id: " + menuTypeId));
    }
}
