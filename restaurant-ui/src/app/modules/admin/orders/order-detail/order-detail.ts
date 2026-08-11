import { Component, inject, OnInit, OnDestroy, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ProgressBarModule } from 'primeng/progressbar';
import { DialogModule } from 'primeng/dialog';
import { TableModule } from 'primeng/table';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { OrderService } from '../orders/order.service';
import { Order, OrderItem, OrderStatus } from '../../../../core/model/order.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { CheckoutDialogComponent } from '../checkout-dialog/checkout-dialog';
import { SplitOrderDialogComponent } from '../split-order-dialog/split-order-dialog';

const STATUS_SEVERITY: Record<OrderStatus, string> = {
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

@Component({
  selector: 'app-order-detail',
  imports: [
    CommonModule, ButtonModule, TagModule, ProgressBarModule, DialogModule,
    TableModule, ConfirmDialogModule, Toast, CardModule, DividerModule,
    CheckoutDialogComponent, SplitOrderDialogComponent,
  ],
  templateUrl: './order-detail.html',
  styleUrls: ['./order-detail.scss'],
  providers: [MessageService, ConfirmationService],
})
export class OrderDetail implements OnInit, OnDestroy {
  readonly orderService = inject(OrderService);
  readonly translate = inject(TranslateService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private confirmationService = inject(ConfirmationService);

  showCheckoutDialog = signal(false);
  showSplitDialog = signal(false);

  private pollingInterval: ReturnType<typeof setInterval> | null = null;

  get order(): Order | null {
    return this.orderService.order();
  }

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.orderService.loadOrderById(id);
    this.orderService.loadTracking(id);
    this.startPolling(id);
  }

  ngOnDestroy() {
    this.stopPolling();
  }

  private startPolling(id: number) {
   /*  this.pollingInterval = setInterval(() => {
      this.orderService.loadTracking(id);
    }, 5000); */
  }

  private stopPolling() {
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval);
      this.pollingInterval = null;
    }
  }

  getStatusSeverity(status: OrderStatus): any {
    return STATUS_SEVERITY[status] ?? 'info';
  }

  canComplete(): boolean {
    return ['NEW', 'CONFIRMED', 'IN_PROGRESS', 'READY'].includes(this.order?.status ?? '');
  }

  canCheckout(): boolean {
    return this.order?.status === 'COMPLETED';
  }

  canSplit(): boolean {
    return !['CHECKED_OUT', 'CANCELLED', 'MERGED'].includes(this.order?.status ?? '');
  }

  canEditItems(): boolean {
    return !['CHECKED_OUT', 'CANCELLED', 'MERGED', 'SPLIT'].includes(this.order?.status ?? '');
  }

  completeOrder() {
    if (!this.order) return;
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_complete_order'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-check-circle',
      accept: () => this.orderService.completeOrder(this.order!.id, () => {
        this.orderService.loadOrderById(this.order!.id);
      }),
    });
  }

  removeItem(item: OrderItem) {
    if (!this.order) return;
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_remove_item'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => this.orderService.removeOrderItem(this.order!.id, item.id),
    });
  }

  incrementItemQty(item: OrderItem) {
    if (!this.order) return;
    this.orderService.updateOrderItemQuantity(this.order.id, item.id, item.quantity + 1);
  }

  decrementItemQty(item: OrderItem) {
    if (!this.order || item.quantity <= 1) return;
    this.orderService.updateOrderItemQuantity(this.order.id, item.id, item.quantity - 1);
  }

  get trackingProgress(): number {
    const tracking = this.orderService.tracking();
    if (!tracking || tracking.totalItems === 0) return 0;
    return Math.round((tracking.readyItems / tracking.totalItems) * 100);
  }

  goBack() {
    this.router.navigate(['/admin/orders']);
  }

  openTracking() {
    if (this.order) this.router.navigate(['/admin/orders', this.order.id, 'tracking']);
  }
}
