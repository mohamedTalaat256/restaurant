import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ProgressBarModule } from 'primeng/progressbar';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { OrderService } from '../orders/order.service';
import { KitchenOrderItemStatus, KitchenOrderStatus, OrderStatus } from '../../../../core/model/order.model';
import { TranslateService } from '../../../../core/service/translate.service';

const ORDER_STATUS_SEVERITY: Record<OrderStatus, string> = {
  NEW: 'info',
  CONFIRMED: 'info',
  IN_PROGRESS: 'warn',
  READY: 'success',
  COMPLETED: 'success',
  CHECKED_OUT: 'contrast',
  CANCELLED: 'danger',
  MERGED: 'secondary',
  SPLIT: 'secondary',
};

const ITEM_STATUS_SEVERITY: Record<KitchenOrderItemStatus, string> = {
  PENDING: 'secondary',
  ACCEPTED: 'info',
  PREPARING: 'warn',
  READY: 'success',
  SERVED: 'contrast',
  REJECTED: 'danger',
};

@Component({
  selector: 'app-order-tracking',
  imports: [CommonModule, ButtonModule, TagModule, ProgressBarModule, CardModule, DividerModule],
  templateUrl: './order-tracking.html',
  styleUrls: ['./order-tracking.scss'],
})
export class OrderTracking implements OnInit, OnDestroy {
  readonly orderService = inject(OrderService);
  readonly translate = inject(TranslateService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  private pollingInterval: ReturnType<typeof setInterval> | null = null;
  orderId = 0;

  ngOnInit() {
    this.orderId = Number(this.route.snapshot.paramMap.get('id'));
    this.orderService.loadTracking(this.orderId);
    this.pollingInterval = setInterval(() => this.orderService.loadTracking(this.orderId), 5000);
  }

  ngOnDestroy() {
    if (this.pollingInterval) clearInterval(this.pollingInterval);
  }

  get progressPercent(): number {
    const t = this.orderService.tracking();
    if (!t || t.totalItems === 0) return 0;
    return Math.round((t.readyItems / t.totalItems) * 100);
  }

  orderStatusSeverity(status: OrderStatus): any {
    return ORDER_STATUS_SEVERITY[status] ?? 'info';
  }

  itemStatusSeverity(status: KitchenOrderItemStatus): any {
    return ITEM_STATUS_SEVERITY[status] ?? 'secondary';
  }

  kitchenStatusSeverity(status: KitchenOrderStatus): any {
    const map: Record<KitchenOrderStatus, string> = {
      PENDING: 'secondary',
      ACCEPTED: 'info',
      PREPARING: 'warn',
      READY: 'success',
    };
    return map[status] ?? 'secondary';
  }

  goBack() {
    this.router.navigate(['/admin/orders', this.orderId]);
  }
}
