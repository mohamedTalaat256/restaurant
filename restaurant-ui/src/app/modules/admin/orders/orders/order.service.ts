import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import {
  Order,
  OrderStatus,
  OrderTracking,
  CreateOrderRequest,
  OrderItemRequest,
  CheckoutRequest,
  SplitOrderRequest,
  MergeOrdersRequest,
  KitchenOrder,
  KitchenOrderStatus,
} from '../../../../core/model/order.model';
import { JournalEntry } from '../../../../core/model/journal-entry.model';
import { env } from '../../../../../environment/env';

@Injectable({ providedIn: 'root' })
export class OrderService {
  orders = signal<Order[]>([]);
  order = signal<Order | null>(null);
  kitchenOrders = signal<KitchenOrder[]>([]);
  kitchenOrder = signal<KitchenOrder | null>(null);
  tracking = signal<OrderTracking | null>(null);
  journalEntry = signal<JournalEntry | null>(null);

  loading = signal(false);
  loadingSave = signal(false);
  savedSuccess = signal(false);

  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  private baseUrl = env.apiUrl + '/orders';

  // ── Orders ─────────────────────────────────────────────

  loadOrders(status?: OrderStatus) {
    this.loading.set(true);
    let params = new HttpParams();
    if (status) params = params.set('orderStatus', status);

    this.http.get<ApiResponse<Order[]>>(this.baseUrl, { params }).subscribe({
      next: (res) => {
        this.orders.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => { this.loading.set(false); }
    });
  }

  loadOrderById(id: number) {
    this.loading.set(true);
    this.http.get<ApiResponse<Order>>(`${this.baseUrl}/${id}`).subscribe({
      next: (res) => {
        this.order.set(res.data);
        this.loading.set(false);
      },
      error: () => { this.loading.set(false); }
    });
  }

  createOrder(req: CreateOrderRequest, onSuccess?: (order: Order) => void) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<Order>>(this.baseUrl, req).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.orders.update(list => [res.data, ...list]);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
          this.savedSuccess.set(true);
          onSuccess?.(res.data);
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  deleteOrder(id: number) {
    this.loading.set(true);
    this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status) {
          this.orders.update(list => list.filter(o => o.id !== id));
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loading.set(false); }
    });
  }

  // ── Order Items ────────────────────────────────────────

  addOrderItem(orderId: number, item: OrderItemRequest) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<Order>>(`${this.baseUrl}/${orderId}/items`, item).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.order.set(res.data);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  removeOrderItem(orderId: number, itemId: number) {
    this.loadingSave.set(true);
    this.http.delete<ApiResponse<Order>>(`${this.baseUrl}/${orderId}/items/${itemId}`).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.order.set(res.data);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  updateOrderItemQuantity(orderId: number, itemId: number, quantity: number) {
    this.loadingSave.set(true);
    let params = new HttpParams().set('quantity', quantity);
    this.http.patch<ApiResponse<Order>>(`${this.baseUrl}/${orderId}/items/${itemId}/quantity`, null, { params }).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.order.set(res.data);
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  // ── Merge / Split ──────────────────────────────────────

  mergeOrders(req: MergeOrdersRequest, onSuccess?: (order: Order) => void) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<Order>>(`${this.baseUrl}/merge`, req).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
          onSuccess?.(res.data);
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  splitOrder(orderId: number, req: SplitOrderRequest, onSuccess?: (orders: Order[]) => void) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<Order[]>>(`${this.baseUrl}/${orderId}/split`, req).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
          onSuccess?.(res.data);
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  // ── Complete / Checkout ────────────────────────────────

  completeOrder(orderId: number, onSuccess?: (order: Order) => void) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<Order>>(`${this.baseUrl}/${orderId}/complete`, {}).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.order.set(res.data);
          this.orders.update(list => list.map(o => o.id === orderId ? res.data : o));
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
          onSuccess?.(res.data);
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  checkoutOrder(orderId: number, req: CheckoutRequest, onSuccess?: (journalEntry: JournalEntry) => void) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<JournalEntry>>(`${this.baseUrl}/${orderId}/checkout`, req).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.journalEntry.set(res.data);
          this.orders.update(list => list.map(o => o.id === orderId ? { ...o, status: 'CHECKED_OUT' } : o));
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
          onSuccess?.(res.data);
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  // ── Tracking ───────────────────────────────────────────

  loadTracking(orderId: number) {
    this.loading.set(true);
    this.http.get<ApiResponse<OrderTracking>>(`${this.baseUrl}/${orderId}/tracking`).subscribe({
      next: (res) => {
        this.tracking.set(res.data);
        this.loading.set(false);
      },
      error: () => { this.loading.set(false); }
    });
  }

  // ── Kitchen Dashboard ──────────────────────────────────

  loadKitchenOrders(kitchenId: number, status?: KitchenOrderStatus) {
    this.loading.set(true);
    const url = status
      ? `${this.baseUrl}/kitchen/by-kitchen/${kitchenId}/status/${status}`
      : `${this.baseUrl}/kitchen/by-kitchen/${kitchenId}`;
    this.http.get<ApiResponse<KitchenOrder[]>>(url).subscribe({
      next: (res) => {
        this.kitchenOrders.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => { this.loading.set(false); }
    });
  }

  kitchenAccept(kitchenOrderId: number, notes?: string) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<KitchenOrder>>(`${this.baseUrl}/kitchen/${kitchenOrderId}/accept`, { notes }).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.kitchenOrders.update(list => list.map(ko => ko.id === kitchenOrderId ? res.data : ko));
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  kitchenReject(kitchenOrderId: number, notes?: string) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<KitchenOrder>>(`${this.baseUrl}/kitchen/${kitchenOrderId}/reject`, { notes }).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.kitchenOrders.update(list => list.map(ko => ko.id === kitchenOrderId ? res.data : ko));
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  kitchenPrepare(kitchenOrderId: number) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<KitchenOrder>>(`${this.baseUrl}/kitchen/${kitchenOrderId}/prepare`, {}).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.kitchenOrders.update(list => list.map(ko => ko.id === kitchenOrderId ? res.data : ko));
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  kitchenReady(kitchenOrderId: number) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<KitchenOrder>>(`${this.baseUrl}/kitchen/${kitchenOrderId}/ready`, {}).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.kitchenOrders.update(list => list.map(ko => ko.id === kitchenOrderId ? res.data : ko));
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  kitchenItemReady(kitchenOrderId: number, itemId: number) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<KitchenOrder>>(`${this.baseUrl}/kitchen/${kitchenOrderId}/items/${itemId}/ready`, {}).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.kitchenOrders.update(list => list.map(ko => ko.id === kitchenOrderId ? res.data : ko));
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  kitchenItemServed(kitchenOrderId: number, itemId: number) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<KitchenOrder>>(`${this.baseUrl}/kitchen/${kitchenOrderId}/items/${itemId}/served`, {}).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.kitchenOrders.update(list => list.map(ko => ko.id === kitchenOrderId ? res.data : ko));
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }
}
