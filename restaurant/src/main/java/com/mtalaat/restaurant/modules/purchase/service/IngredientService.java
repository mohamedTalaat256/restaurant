package com.mtalaat.restaurant.modules.purchase.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.purchase.dto.IngredientDto;
import com.mtalaat.restaurant.modules.purchase.entity.Ingredient;
import com.mtalaat.restaurant.modules.purchase.mapping.IngredientMapper;
import com.mtalaat.restaurant.modules.purchase.repository.IngredientRepository;
import com.mtalaat.restaurant.modules.settings.entity.UnitOfMeasurement;
import com.mtalaat.restaurant.modules.settings.repository.UnitOfMeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final UnitOfMeasurementRepository unitOfMeasurementRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientDto create(IngredientDto dto) {
        UnitOfMeasurement uom = unitOfMeasurementRepository.findById(dto.getUomId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit of measurement not found with id: " + dto.getUomId()));
        Ingredient entity = ingredientMapper.toEntity(dto, uom);
        return ingredientMapper.toDto(ingredientRepository.save(entity));
    }

    public IngredientDto getById(Long id) {
        Ingredient entity = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + id));
        return ingredientMapper.toDto(entity);
    }

    public List<IngredientDto> getAll() {
        return ingredientRepository.findAll().stream().map(ingredientMapper::toDto).toList();
    }

    public IngredientDto update(Long id, IngredientDto dto) {
        Ingredient entity = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + id));
        UnitOfMeasurement uom = unitOfMeasurementRepository.findById(dto.getUomId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit of measurement not found with id: " + dto.getUomId()));
        entity.setName(dto.getName());
        entity.setUom(uom);
        entity.setStockQuantity(dto.getStockQuantity());
        entity.setMinStockQuantity(dto.getMinStockQuantity());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());
        return ingredientMapper.toDto(ingredientRepository.save(entity));
    }

    public void delete(Long id) {
        Ingredient entity = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + id));
        ingredientRepository.delete(entity);
    }
}
