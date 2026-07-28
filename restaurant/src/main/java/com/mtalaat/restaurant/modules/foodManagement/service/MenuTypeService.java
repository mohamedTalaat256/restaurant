package com.mtalaat.restaurant.modules.foodManagement.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.foodManagement.dto.MenuTypeDto;
import com.mtalaat.restaurant.modules.foodManagement.entity.MenuType;
import com.mtalaat.restaurant.modules.foodManagement.mapping.MenuTypeMapper;
import com.mtalaat.restaurant.modules.foodManagement.repository.MenuTypeRepository;
import com.mtalaat.restaurant.utility.FileUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuTypeService {

    private final MenuTypeRepository menuTypeRepository;
    private final MenuTypeMapper menuTypeMapper;
    private final FileUtility fileUtility;

    public MenuTypeDto add(MenuTypeDto dto, MultipartFile image) {
        return create(dto, image);
    }

    public MenuTypeDto create(MenuTypeDto dto, MultipartFile image) {
        MenuType entity = menuTypeMapper.toEntity(dto);

        if (image != null && !image.isEmpty()) {
            String imagePath = fileUtility.saveFile(image, "food");
            entity.setImage(imagePath);
        }

        return menuTypeMapper.toDto(menuTypeRepository.save(entity));
    }

    public MenuTypeDto getById(Long id) {
        MenuType entity = menuTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuType not found with id: " + id));
        return menuTypeMapper.toDto(entity);
    }

    public List<MenuTypeDto> getAll() {
        return menuTypeRepository.findAll().stream().map(menuTypeMapper::toDto).toList();
    }

    public MenuTypeDto update(Long id, MenuTypeDto dto, MultipartFile image) {
        MenuType entity = menuTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuType not found with id: " + id));

        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());

        if (image != null && !image.isEmpty()) {
            String imagePath = fileUtility.saveFile(image, "food");
            entity.setImage(imagePath);
        }

        return menuTypeMapper.toDto(menuTypeRepository.save(entity));
    }

    public void delete(Long id) {
        MenuType entity = menuTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuType not found with id: " + id));
        menuTypeRepository.delete(entity);
    }
}
