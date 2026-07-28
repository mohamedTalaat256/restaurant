package com.mtalaat.restaurant.modules.account.mapper;

import com.mtalaat.restaurant.modules.account.dto.AccountDTO;
import com.mtalaat.restaurant.modules.account.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDTO toDTO(Account account) {
        if (account == null) return null;
        return AccountDTO.builder()
                .id(account.getId())
                .code(account.getCode())
                .name(account.getName())
                .type(account.getType())
                .allowTransaction(account.getAllowTransaction())
                .status(account.getStatus())
                .balance(account.getBalance())
                .parentId(account.getParent() != null ? account.getParent().getId() : null)
                .parentName(account.getParent() != null ? account.getParent().getName() : null)
                .build();
    }
}