import { Component, inject, OnInit } from '@angular/core';
import { StatsWidget } from "./components/statswidget";
import { RecentSalesWidget } from "./components/recentsaleswidget";
import { BestSellingWidget } from "./components/bestsellingwidget";
import { RevenueStreamWidget } from "./components/revenuestreamwidget";
import { NotificationsWidget } from "./components/notificationswidget";
import { DashboardService } from './dashboard.service';


@Component({
  selector: 'app-admin-dashboard',
  imports: [StatsWidget, RecentSalesWidget, BestSellingWidget, RevenueStreamWidget, NotificationsWidget],
    template: `
        <div class="grid grid-cols-12 gap-8">
            <app-stats-widget
            [totalOrders]="dashboardService.dashboardData().totalOrders"
            [totalOrdersLast24Hours]="dashboardService.dashboardData().totalOrdersLast24Hours"
            [totalRevenue]="dashboardService.dashboardData().totalRevenue"
            [totalCustomers]="dashboardService.dashboardData().totalCustomers"
            [totalProducts]="dashboardService.dashboardData().totalProducts"
             class="contents" />
            <div class="col-span-12 xl:col-span-6">
                <app-recent-sales-widget [topSellingItems]="dashboardService.dashboardData().topSellingItems || []" />
                <app-best-selling-widget />
            </div>
            <div class="col-span-12 xl:col-span-6">
                <app-revenue-stream-widget />
                <app-notifications-widget />
            </div>
        </div>
    `
})
export class AdminDashboard implements OnInit {
  readonly dashboardService = inject(DashboardService);

  ngOnInit() {
    this.dashboardService.loadDashboardData();
  }
}
