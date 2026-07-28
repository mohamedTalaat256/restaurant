package com.mtalaat.restaurant.modules.settings.mapping;

import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.settings.dto.CustomerDto;
import com.mtalaat.restaurant.modules.settings.entity.Customer;
import com.mtalaat.restaurant.modules.settings.entity.CustomerType;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDto toDto(Customer entity) {
        return CustomerDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .favoriteDeliveryAddress(entity.getFavoriteDeliveryAddress())
                .customerType(entity.getCustomerType() != null ? entity.getCustomerType().getType() : null)
                .customerTypeDescription(entity.getCustomerType() != null ? entity.getCustomerType().getDescription() : null)
                .status(entity.getStatus())
                .allowCredit(entity.getAllowCredit())
                .accountId(entity.getAccount()!= null ? entity.getAccount().getId() : null)
                .accountCode(entity.getAccount()!= null ?entity.getAccount().getCode() : null)
                .accountName(entity.getAccount()!= null ?entity.getAccount().getName() : null)
                .build();
    }

    public Customer toEntity(CustomerDto dto, CustomerType customerType) {
        return Customer.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .favoriteDeliveryAddress(dto.getFavoriteDeliveryAddress())
                .password(dto.getPassword())
                .customerType(customerType)
                .allowCredit(dto.getAllowCredit())
                .status(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus())
                .build();
    }


    public Customer toEntity(CustomerDto dto, CustomerType customerType, Account customerAccount) {
        return Customer.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .favoriteDeliveryAddress(dto.getFavoriteDeliveryAddress())
                .password(dto.getPassword())
                .customerType(customerType)
                .allowCredit(dto.getAllowCredit())
                .status(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus())
                .account(customerAccount)
                .build();
    }
}
