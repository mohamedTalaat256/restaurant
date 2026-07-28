package com.mtalaat.restaurant.modules.auth.service;

import com.mtalaat.restaurant.modules.auth.dto.MenuItemDto;
import com.mtalaat.restaurant.modules.auth.entity.MenuItem;
import com.mtalaat.restaurant.modules.auth.entity.ModuleEntity;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.auth.mapping.MenuItemMapper;
import com.mtalaat.restaurant.modules.auth.repository.MenuItemRepository;
import com.mtalaat.restaurant.modules.auth.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final ModuleRepository moduleRepository;
    private final MenuItemMapper menuItemMapper;

    public MenuItemDto create(MenuItemDto dto) {
        MenuItem entity = menuItemMapper.toEntity(dto);
        applyRelations(entity, dto);
        return menuItemMapper.toDto(menuItemRepository.save(entity));
    }

    public MenuItemDto getById(Long id) {
        MenuItem entity = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        return menuItemMapper.toDto(entity);
    }

    public List<MenuItemDto> getAll() {
        return menuItemRepository.findAll().stream().map(menuItemMapper::toDto).toList();
    }

    public MenuItemDto update(Long id, MenuItemDto dto) {
        MenuItem existing = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        existing.setTitle(dto.getTitle());
        existing.setIsPage(dto.getIsPage());
        existing.setPageUrl(dto.getPageUrl());
        existing.setIsReport(dto.getIsReport());
        applyRelations(existing, dto);

        return menuItemMapper.toDto(menuItemRepository.save(existing));
    }

    public void delete(Long id) {
        MenuItem existing = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        menuItemRepository.delete(existing);
    }

    private void applyRelations(MenuItem menuItem, MenuItemDto dto) {
        if (dto.getModuleId() != null) {
            ModuleEntity module = moduleRepository.findById(dto.getModuleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + dto.getModuleId()));
            menuItem.setModule(module);
        } else {
            menuItem.setModule(null);
        }

        if (dto.getParentMenuId() != null) {
            MenuItem parent = menuItemRepository.findById(dto.getParentMenuId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent menu not found with id: " + dto.getParentMenuId()));
            menuItem.setParentMenu(parent);
        } else {
            menuItem.setParentMenu(null);
        }
    }
}
