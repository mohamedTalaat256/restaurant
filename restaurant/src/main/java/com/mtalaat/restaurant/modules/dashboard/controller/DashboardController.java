package com.mtalaat.restaurant.modules.dashboard.controller;


import com.mtalaat.restaurant.modules.dashboard.dto.DashboardDto;
import com.mtalaat.restaurant.modules.foodManagement.service.ItemFoodService;
import com.mtalaat.restaurant.modules.order.service.OrderService;
import com.mtalaat.restaurant.modules.settings.service.CustomerService;
import com.mtalaat.restaurant.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ItemFoodService itemFoodService;


    @GetMapping("/data")
    public ResponseEntity<ApiResponse> getDashboardData() {

        DashboardDto dashboardData = new DashboardDto();
        dashboardData.setTotalOrders(orderService.getOrdersCount());
        dashboardData.setTotalOrdersLast24Hours(orderService.getOrdersCountLast24Hours());
        dashboardData.setTotalCustomers(customerService.getCustomersCount());
        dashboardData.setTotalProducts(itemFoodService.getProductsCount());
        dashboardData.setTopSellingItems(orderService.getTopSellingItems(5)); // Get top 5 selling items

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("msg_dashboard_data_fetched", dashboardData, HttpStatus.OK.value()));
    }
}
