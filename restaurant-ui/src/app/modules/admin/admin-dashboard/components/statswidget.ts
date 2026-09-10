import { Component, inject, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateService } from '../../../../core/service/translate.service';

@Component({
    standalone: true,
    selector: 'app-stats-widget',
    imports: [CommonModule],
    template: `<div class="col-span-12 lg:col-span-6 xl:col-span-3">
            <div class="card mb-0">
                <div class="flex justify-between mb-4">
                    <div>
                        <span class="block text-muted-color font-medium mb-4">{{translate.instant('label_orders')}}</span>
                        <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">{{totalOrders}}</div>
                    </div>
                    <div class="flex items-center justify-center bg-blue-100 dark:bg-blue-400/10 rounded-border" style="width: 2.5rem; height: 2.5rem">
                        <i class="pi pi-shopping-cart text-blue-500 text-xl!"></i>
                    </div>
                </div>
                <span class="text-primary font-medium">{{totalOrdersLast24Hours}} </span>
                <span class="text-muted-color">{{translate.instant('last_24_hours')}}</span>
            </div>
        </div>
        <div class="col-span-12 lg:col-span-6 xl:col-span-3">
            <div class="card mb-0">
                <div class="flex justify-between mb-4">
                    <div>
                        <span class="block text-muted-color font-medium mb-4">{{translate.instant('label_revenue')}}</span>
                        <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">{{totalRevenue}}</div>
                    </div>
                    <div class="flex items-center justify-center bg-orange-100 dark:bg-orange-400/10 rounded-border" style="width: 2.5rem; height: 2.5rem">
                        <i class="pi pi-dollar text-orange-500 text-xl!"></i>
                    </div>
                </div>
                <span class="text-primary font-medium">%52+ </span>
                <span class="text-muted-color">{{translate.instant('last_week')}}</span>
            </div>
        </div>
        <div class="col-span-12 lg:col-span-6 xl:col-span-3">
            <div class="card mb-0">
                <div class="flex justify-between mb-4">
                    <div>
                        <span class="block text-muted-color font-medium mb-4">{{translate.instant('label_customers')}}</span>
                        <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">{{totalCustomers}}</div>
                    </div>
                    <div class="flex items-center justify-center bg-cyan-100 dark:bg-cyan-400/10 rounded-border" style="width: 2.5rem; height: 2.5rem">
                        <i class="pi pi-users text-cyan-500 text-xl!"></i>
                    </div>
                </div>
                <span class="text-primary font-medium">520 </span>
                <span class="text-muted-color">{{translate.instant('newly_registered')}}</span>
            </div>
        </div>
        <div class="col-span-12 lg:col-span-6 xl:col-span-3">
            <div class="card mb-0">
                <div class="flex justify-between mb-4">
                    <div>
                        <span class="block text-muted-color font-medium mb-4">{{translate.instant('label_item_food')}}</span>
                        <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">{{totalProducts}}</div>
                        <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">152 Unread</div>
                    </div>
                    <div class="flex items-center justify-center bg-purple-100 dark:bg-purple-400/10 rounded-border" style="width: 2.5rem; height: 2.5rem">
                        <i class="pi pi-comment text-purple-500 text-xl!"></i>
                    </div>
                </div>
                <span class="text-primary font-medium">85 </span>
                <span class="text-muted-color">{{translate.instant('responded')}}</span>
            </div>
        </div>`
})
export class StatsWidget {

  readonly translate = inject(TranslateService);


  //convert the following to signal input

  @Input() totalOrders: number = 0;
  @Input() totalOrdersLast24Hours: number = 0;
  @Input() totalRevenue: number = 0;
  @Input() totalCustomers: number = 0;
  @Input() totalProducts: number = 0;


}
