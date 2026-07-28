package com.mtalaat.restaurant.modules.purchase.mapping;

import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.purchase.dto.SupplierDto;
import com.mtalaat.restaurant.modules.purchase.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierDto toDto(Supplier entity) {
        return SupplierDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .status(entity.getStatus())
                .accountId(entity.getAccount().getId())
                .accountCode(entity.getAccount().getCode())
                .accountName(entity.getAccount().getName())
                .accountBalance(entity.getAccount().getBalance())
                .build();
    }

    public Supplier toEntity(SupplierDto dto) {
        Supplier entity = new Supplier();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());
        return entity;
    }

    public Supplier toEntity(SupplierDto dto, Account account) {
        Supplier entity = new Supplier();

        entity.setAccount(account);
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());
        return entity;
    }
}
