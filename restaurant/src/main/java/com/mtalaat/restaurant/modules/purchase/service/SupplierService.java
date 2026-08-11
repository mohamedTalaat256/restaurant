package com.mtalaat.restaurant.modules.purchase.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.service.AccountParentCodes;
import com.mtalaat.restaurant.modules.account.service.AccountService;
import com.mtalaat.restaurant.modules.purchase.dto.SupplierDto;
import com.mtalaat.restaurant.modules.purchase.entity.Supplier;
import com.mtalaat.restaurant.modules.purchase.mapping.SupplierMapper;
import com.mtalaat.restaurant.modules.purchase.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final AccountService accountService;
    private final AccountParentCodes parentCodes;

    @Transactional
    public SupplierDto create(SupplierDto dto) {
        Account supplierAccount = accountService.createSubAccount(
                parentCodes.getSuppliers(),
                "مورد - " + dto.getName()
        );
        Supplier entity = supplierMapper.toEntity(dto,supplierAccount);
        return supplierMapper.toDto(supplierRepository.save(entity));
    }

    public SupplierDto getById(Long id) {
        Supplier entity = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return supplierMapper.toDto(entity);
    }

    public List<SupplierDto> getAll() {
        return supplierRepository.findAll().stream().map(supplierMapper::toDto).toList();
    }

    public SupplierDto update(Long id, SupplierDto dto) {
        Supplier entity = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());

        //update the associated account name if the supplier name changes

        accountService.updateAccountName(entity.getAccount().getId(), "مورد - " + dto.getName());


        return supplierMapper.toDto(supplierRepository.save(entity));
    }

    public void delete(Long id) {
        Supplier entity = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        supplierRepository.delete(entity);
    }
}
