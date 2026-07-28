package com.mtalaat.restaurant.modules.account.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostCenterDTO {

    private Long id;
    private String code;
    private String name;
    private Boolean status;
}
