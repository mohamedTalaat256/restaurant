package com.mtalaat.restaurant.modules.account.service;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.account.dto.CostCenterDTO;
import com.mtalaat.restaurant.modules.account.entity.CostCenter;
import com.mtalaat.restaurant.modules.account.mapper.CostCenterMapper;
import com.mtalaat.restaurant.modules.account.repository.CostCenterRepository;
import com.mtalaat.restaurant.modules.settings.service.LanguageTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CostCenterService {

    private final CostCenterRepository costCenterRepository;
    private final CostCenterMapper costCenterMapper;
    private final LanguageTranslationService translate;

    @Transactional(readOnly = true)
    public List<CostCenterDTO> getAll() {
        return costCenterRepository.findAll().stream()
                .map(costCenterMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CostCenterDTO> getActive() {
        return costCenterRepository.findByStatusTrue().stream()
                .map(costCenterMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CostCenterDTO getById(Long id) {
        CostCenter entity = costCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(translate.get("error_cost_center_not_found")));
        return costCenterMapper.toDTO(entity);
    }

    @Transactional
    public CostCenterDTO create(CostCenterDTO dto) {
        if (costCenterRepository.existsByCode(dto.getCode())) {
            throw new BadRequestException(translate.get("error_cost_center_code_exists"));
        }
        CostCenter entity = costCenterMapper.toEntity(dto);
        return costCenterMapper.toDTO(costCenterRepository.save(entity));
    }

    @Transactional
    public CostCenterDTO update(Long id, CostCenterDTO dto) {
        CostCenter entity = costCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(translate.get("error_cost_center_not_found")));

        costCenterRepository.findByCode(dto.getCode()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BadRequestException(translate.get("error_cost_center_code_exists"));
            }
        });

        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        return costCenterMapper.toDTO(costCenterRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        CostCenter entity = costCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(translate.get("error_cost_center_not_found")));
        entity.setStatus(false);
        costCenterRepository.save(entity);
    }
}
