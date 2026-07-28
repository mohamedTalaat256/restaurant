import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { CardModule } from 'primeng/card';
import { ToolbarModule } from 'primeng/toolbar';
import { SelectModule } from 'primeng/select';
import { FormsModule } from '@angular/forms';
import { Toast } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { TooltipModule } from 'primeng/tooltip';
import { OrderService } from '../orders/order.service';
import { KitchenOrder, KitchenOrderItemStatus, KitchenOrderStatus } from '../../../../core/model/order.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { KitchenService } from '../../settings/kitchens/kitchen.service';

const ITEM_STATUS_SEVERITY: Record<KitchenOrderItemStatus, string> = {
  PENDING: 'secondary',
  ACCEPTED: 'info',
  PREPARING: 'warn',
  READY: 'success',
  SERVED: 'contrast',
  REJECTED: 'danger',
};

@Component({
  selector: 'app-kitchen-dashboard',
  imports: [CommonModule, ButtonModule, TagModule, CardModule, ToolbarModule, SelectModule, FormsModule, Toast, TooltipModule],
  templateUrl: './kitchen-dashboard.html',
  styleUrls: ['./kitchen-dashboard.scss'],
  providers: [MessageService],
})
export class KitchenDashboard implements OnInit, OnDestroy {
  readonly orderService = inject(OrderService);
  readonly translate = inject(TranslateService);
  readonly kitchenService = inject(KitchenService);
  private route = inject(ActivatedRoute);

  kitchenId = 0;
  private pollingInterval: ReturnType<typeof setInterval> | null = null;

  columns: { status: KitchenOrderStatus; label: string }[] = [
    { status: 'PENDING', label: 'label_pending' },
    { status: 'ACCEPTED', label: 'label_accepted' },
    { status: 'PREPARING', label: 'label_preparing' },
    { status: 'READY', label: 'label_ready' },
  ];

  ngOnInit() {
   // this.kitchenId = Number(this.route.snapshot.paramMap.get('kitchenId'));
   // this.loadOrders();
    this.pollingInterval = setInterval(() => this.loadOrders(), 5000);
    this.kitchenService.loadKitchens();
  }

  selectKitchen(kitchenId: number) {
    this.kitchenId = kitchenId;
    this.loadOrders();
  }

  ngOnDestroy() {
    if (this.pollingInterval) clearInterval(this.pollingInterval);
  }

  loadOrders() {
    this.orderService.loadKitchenOrders(this.kitchenId);
  }

  getOrdersByStatus(status: KitchenOrderStatus): KitchenOrder[] {
    return this.orderService.kitchenOrders().filter(o => o.status === status);
  }

  itemStatusSeverity(status: KitchenOrderItemStatus): any {
    return ITEM_STATUS_SEVERITY[status] ?? 'secondary';
  }

  accept(ko: KitchenOrder) { this.orderService.kitchenAccept(ko.id); }
  reject(ko: KitchenOrder) { this.orderService.kitchenReject(ko.id); }
  prepare(ko: KitchenOrder) { this.orderService.kitchenPrepare(ko.id); }
  markReady(ko: KitchenOrder) { this.orderService.kitchenReady(ko.id); }
  markItemReady(ko: KitchenOrder, itemId: number) { this.orderService.kitchenItemReady(ko.id, itemId); }
  markItemServed(ko: KitchenOrder, itemId: number) { this.orderService.kitchenItemServed(ko.id, itemId); }

  canAccept(ko: KitchenOrder): boolean { return ko.status === 'PENDING'; }
  canReject(ko: KitchenOrder): boolean { return ko.status === 'PENDING'; }
  canPrepare(ko: KitchenOrder): boolean { return ko.status === 'ACCEPTED'; }
  canMarkReady(ko: KitchenOrder): boolean { return ['ACCEPTED', 'PREPARING'].includes(ko.status); }
  canItemReady(status: KitchenOrderItemStatus): boolean { return ['ACCEPTED', 'PREPARING'].includes(status); }
  canItemServed(status: KitchenOrderItemStatus): boolean { return status === 'READY'; }
}
