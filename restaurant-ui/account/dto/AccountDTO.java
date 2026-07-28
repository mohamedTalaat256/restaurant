package com.mtalaat.restaurant.modules.account.dto;

import com.mtalaat.restaurant.modules.account.enums.AccountType;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountDTO {
    private Long id;
    private String code;
    private String name;
    private AccountType type;
    private Boolean allowTransaction;
    private Boolean status;
    private BigDecimal balance;
    private Long parentId;
    private String parentName;
    private List<AccountDTO> children;
}