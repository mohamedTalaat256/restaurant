import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { SelectModule } from 'primeng/select';
import { FormsModule } from '@angular/forms';
import { TooltipModule } from 'primeng/tooltip';
import { OrderService } from './order.service';
import { Order, OrderStatus } from '../../../../core/model/order.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { CheckoutDialogComponent } from '../checkout-dialog/checkout-dialog';

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
  selector: 'app-orders',
  imports: [
    CommonModule, TableModule, ButtonModule, ToolbarModule, InputTextModule,
    TagModule, InputIconModule, IconFieldModule, ConfirmDialogModule,
    Toast, SelectModule, FormsModule, TooltipModule, CheckoutDialogComponent,
  ],
  templateUrl: './orders.html',
  styleUrls: ['./orders.scss'],
  providers: [MessageService, ConfirmationService],
})
export class Orders implements OnInit {
  @ViewChild('dt') dt!: Table;

  readonly orderService = inject(OrderService);
  readonly translate = inject(TranslateService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private confirmationService = inject(ConfirmationService);

  selectedOrders: Order[] = [];
  selectedStatus: OrderStatus | null = null;
  page = signal(0);
  size = signal(10);
  showCheckoutDialog = signal(false);
  checkoutOrderId = signal<number | null>(null);
  checkoutTotal = signal<number>(0);

  statusOptions = [
    { label: this.translate.instant('label_all'), value: null },
    ...(['NEW', 'CONFIRMED', 'IN_PROGRESS', 'READY', 'COMPLETED', 'CHECKED_OUT', 'CANCELLED', 'MERGED', 'SPLIT'] as OrderStatus[])
      .map(s => ({ label: this.translate.instant(s), value: s })),
  ];

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.selectedStatus = params['status'] ?? null;
      this.loadOrders();
    });
  }

  loadOrders() {
    this.orderService.loadOrders(this.selectedStatus ?? undefined, this.page(), this.size());
  }

  onStatusChange() {
    this.page.set(0);
    this.loadOrders();
  }

  onPageChange(event: any) {
    this.page.set(Math.floor(event.first / event.rows));
    this.size.set(event.rows);
    this.loadOrders();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  viewOrder(order: Order) {
    this.router.navigate(['/admin/orders', order.id]);
  }

  completeOrder(order: Order) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_complete_order'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-check-circle',
      accept: () => this.orderService.completeOrder(order.id),
    });
  }

  openCheckout(order: Order) {
    this.checkoutOrderId.set(order.id);
    this.checkoutTotal.set(order.totalAmount);
    this.showCheckoutDialog.set(true);
  }

  deleteOrder(order: Order) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_order'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => this.orderService.deleteOrder(order.id),
    });
  }

  mergeSelected() {
    if (this.selectedOrders.length < 2) return;
    const forbidden: OrderStatus[] = ['COMPLETED', 'CHECKED_OUT', 'CANCELLED', 'MERGED'];
    const invalid = this.selectedOrders.some(o => forbidden.includes(o.status));
    if (invalid) {
      return;
    }
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_merge_orders'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-code-branch',
      accept: () => {
        this.orderService.mergeOrders(
          { orderIds: this.selectedOrders.map(o => o.id) },
          (merged) => {
            this.selectedOrders = [];
            this.router.navigate(['/admin/orders', merged.id]);
          }
        );
      },
    });
  }

  getStatusSeverity(status: OrderStatus): any {
    return STATUS_SEVERITY[status] ?? 'info';
  }

  canComplete(status: OrderStatus): boolean {
    return ['NEW', 'CONFIRMED', 'IN_PROGRESS', 'READY'].includes(status);
  }

  canCheckout(status: OrderStatus): boolean {
    return status === 'COMPLETED';
  }

  canDelete(status: OrderStatus): boolean {
    return !['CHECKED_OUT', 'MERGED'].includes(status);
  }
}
