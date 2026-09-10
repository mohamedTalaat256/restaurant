package com.mtalaat.restaurant.modules.dashboard.dto;


import com.mtalaat.restaurant.modules.foodManagement.dto.ItemFoodDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {

    private Long totalOrders;
    private Long totalOrdersLast24Hours;
    private Long totalRevenue;
    private Long totalCustomers;
    private Long totalProducts;

    private List<ItemFoodDto> topSellingItems;

}
