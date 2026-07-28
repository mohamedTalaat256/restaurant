package com.mtalaat.restaurant.modules.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MergeOrdersDto {

    @NotEmpty(message = "At least two order IDs are required to merge")
    @Size(min = 2, message = "At least two order IDs are required to merge")
    private List<Long> orderIds;

    private String notes;
}
