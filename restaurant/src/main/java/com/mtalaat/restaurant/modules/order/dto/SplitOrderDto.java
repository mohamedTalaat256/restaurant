package com.mtalaat.restaurant.modules.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SplitOrderDto {

    /**
     * Each inner list represents one new split order, containing the items to move into it.
     */
    @NotEmpty(message = "At least one split group is required")
    @Valid
    private List<List<SplitOrderItemDto>> splitGroups;

    private String notes;
}
