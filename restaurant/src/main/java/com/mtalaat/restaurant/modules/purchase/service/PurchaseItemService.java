package com.mtalaat.restaurant.modules.purchase.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.purchase.dto.PurchaseItemDto;
import com.mtalaat.restaurant.modules.purchase.entity.Ingredient;
import com.mtalaat.restaurant.modules.purchase.entity.PurchaseItem;
import com.mtalaat.restaurant.modules.purchase.mapping.PurchaseItemMapper;
import com.mtalaat.restaurant.modules.purchase.repository.IngredientRepository;
import com.mtalaat.restaurant.modules.purchase.repository.PurchaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;
    private final IngredientRepository ingredientRepository;
    private final PurchaseItemMapper purchaseItemMapper;

    public PurchaseItemDto create(PurchaseItemDto dto) {
        PurchaseItem entity = purchaseItemMapper.toEntity(dto);
        return purchaseItemMapper.toDto(purchaseItemRepository.save(entity));
    }

    public PurchaseItemDto getById(Long id) {
        PurchaseItem entity = purchaseItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase item not found with id: " + id));
        return purchaseItemMapper.toDto(entity);
    }

    public List<PurchaseItemDto> getAll() {
        return purchaseItemRepository.findAll().stream().map(purchaseItemMapper::toDto).toList();
    }

    public PurchaseItemDto update(Long id, PurchaseItemDto dto) {
        PurchaseItem entity = purchaseItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase item not found with id: " + id));

        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + dto.getIngredientId()));
         entity.setIngredient(ingredient);
        entity.setQuantity(dto.getQuantity());
        entity.setPrice(dto.getPrice());
        return purchaseItemMapper.toDto(purchaseItemRepository.save(entity));
    }

    public void delete(Long id) {
        PurchaseItem entity = purchaseItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase item not found with id: " + id));
        purchaseItemRepository.delete(entity);
    }
}
