package com.mtalaat.restaurant.modules.settings.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.service.AccountParentCodes;
import com.mtalaat.restaurant.modules.account.service.AccountService;
import com.mtalaat.restaurant.modules.settings.dto.CustomerDto;
import com.mtalaat.restaurant.modules.settings.entity.Customer;
import com.mtalaat.restaurant.modules.settings.entity.CustomerType;
import com.mtalaat.restaurant.modules.settings.mapping.CustomerMapper;
import com.mtalaat.restaurant.modules.settings.repository.CustomerRepository;
import com.mtalaat.restaurant.modules.settings.repository.CustomerTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerTypeRepository customerTypeRepository;
    private final CustomerMapper customerMapper;

    private final AccountService accountService;
    private final AccountParentCodes parentCodes;


    @Transactional
    public CustomerDto create(CustomerDto dto) {
        CustomerType customerType = customerTypeRepository.findById(dto.getCustomerType())
                .orElseThrow(() -> new ResourceNotFoundException("Customer type not found with type: " + dto.getCustomerType()));

        Account customerAccount = null;
        // تحقق: إذا كان العميل يُسمح له بالبيع الآجل (الائتمان) أو شركة تعاقدات
        if (Boolean.TRUE.equals(dto.getAllowCredit())) {
            customerAccount = accountService.createSubAccount(
                    parentCodes.getCustomers(),
                    "عميل آجل - " + dto.getName()
            );

        }

        Customer entity = customerMapper.toEntity(dto, customerType, customerAccount);


        return customerMapper.toDto(customerRepository.save(entity));
    }

    public CustomerDto getById(Long id) {
        Customer entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return customerMapper.toDto(entity);
    }

    public List<CustomerDto> getAll() {
        return customerRepository.findAll().stream().map(customerMapper::toDto).toList();
    }

    public CustomerDto update(Long id, CustomerDto dto) {
        Customer entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        CustomerType customerType = customerTypeRepository.findById(dto.getCustomerType())
                .orElseThrow(() -> new ResourceNotFoundException("Customer type not found with type: " + dto.getCustomerType()));
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setFavoriteDeliveryAddress(dto.getFavoriteDeliveryAddress());
        if (dto.getPassword() != null) {
            entity.setPassword(dto.getPassword());
        }
        entity.setCustomerType(customerType);
        entity.setStatus(dto.getStatus() == null ? Boolean.TRUE : dto.getStatus());
        return customerMapper.toDto(customerRepository.save(entity));
    }

    public void delete(Long id) {
        Customer entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(entity);
    }



}
