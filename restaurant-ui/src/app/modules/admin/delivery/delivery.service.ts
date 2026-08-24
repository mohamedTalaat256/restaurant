import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../core/service/translate.service';
import { ApiResponse } from '../../../core/model/api-response.model';
import {
  Delivery,
  DeliveryStatus,
  DeliveryTrackingPoint,
  CreateDeliveryRequest,
  AssignDriverRequest,
  ReassignDriverRequest,
  CancelDeliveryRequest,
} from '../../../core/model/delivery.model';
import { env } from '../../../../environment/env';

const ERROR_KEYS: Record<string, string> = {
  msg_order_not_delivery_type: 'This order is not a delivery order',
  msg_delivery_already_exists: 'A delivery already exists for this order',
  msg_driver_not_available: 'Driver is not available for assignment',
  msg_driver_has_active_delivery: 'Driver already has an active delivery',
  msg_delivery_in_terminal_state: 'This delivery is already closed',
  msg_order_not_ready_for_pickup: 'The order is not ready for pickup yet',
  msg_driver_busy_cannot_change_status: 'Cannot change status while driver has an active delivery',
  msg_no_active_delivery: 'Driver has no active delivery',
  msg_no_tracking_data: 'No tracking data available yet',
  msg_delivery_not_found: 'Delivery not found',
  msg_driver_not_found: 'Driver not found',
};

@Injectable({ providedIn: 'root' })
export class DeliveryService {
  deliveries = signal<Delivery[]>([]);
  delivery = signal<Delivery | null>(null);
  trackingPoints = signal<DeliveryTrackingPoint[]>([]);
  latestPoint = signal<DeliveryTrackingPoint | null>(null);

  loading = signal(false);
  loadingSave = signal(false);
  savedSuccess = signal(false);

  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  private baseUrl = env.apiUrl + '/deliveries';

  private resolveError(key: string): string {
    const translated = this.translate.instant(key);
    return translated !== key ? translated : (ERROR_KEYS[key] ?? key);
  }

  loadDeliveries(status?: DeliveryStatus) {
    this.loading.set(true);
    const url = status ? `${this.baseUrl}?status=${status}` : this.baseUrl;
    this.http.get<ApiResponse<Delivery[]>>(url).subscribe({
      next: (res) => {
        this.deliveries.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      },
    });
  }

  loadDeliveryById(id: number) {
    this.loading.set(true);
    this.http.get<ApiResponse<Delivery>>(`${this.baseUrl}/${id}`).subscribe({
      next: (res) => {
        this.delivery.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      },
    });
  }

  loadDeliveryByOrderId(orderId: number) {
    this.loading.set(true);
    this.http.get<ApiResponse<Delivery>>(`${this.baseUrl}/order/${orderId}`).subscribe({
      next: (res) => {
        this.delivery.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      },
    });
  }

  createDelivery(req: CreateDeliveryRequest, onSuccess?: (d: Delivery) => void) {
    this.loadingSave.set(true);
    this.savedSuccess.set(false);
    this.http.post<ApiResponse<Delivery>>(this.baseUrl, req).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.deliveries.update((list) => [res.data, ...list]);
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: res.message,
            life: 3000,
          });
          this.savedSuccess.set(true);
          onSuccess?.(res.data);
        } else {
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant('label_failed'),
            detail: this.resolveError(res.message),
            life: 3000,
          });
        }
      },
      error: (err) => {
        this.loadingSave.set(false);
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant('label_failed'),
          detail: this.resolveError(err?.error?.message ?? 'label_error'),
          life: 3000,
        });
      },
    });
  }

  assignDriver(deliveryId: number, req: AssignDriverRequest, onSuccess?: () => void) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<Delivery>>(`${this.baseUrl}/${deliveryId}/assign`, req).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.delivery.set(res.data);
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: res.message,
            life: 3000,
          });
          onSuccess?.();
        } else {
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant('label_failed'),
            detail: this.resolveError(res.message),
            life: 3000,
          });
        }
      },
      error: (err) => {
        this.loadingSave.set(false);
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant('label_failed'),
          detail: this.resolveError(err?.error?.message ?? 'label_error'),
          life: 3000,
        });
      },
    });
  }

  reassignDriver(deliveryId: number, req: ReassignDriverRequest, onSuccess?: () => void) {
    this.loadingSave.set(true);
    this.http.post<ApiResponse<Delivery>>(`${this.baseUrl}/${deliveryId}/reassign`, req).subscribe({
      next: (res) => {
        this.loadingSave.set(false);
        if (res.status) {
          this.delivery.set(res.data);
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: res.message,
            life: 3000,
          });
          onSuccess?.();
        } else {
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant('label_failed'),
            detail: this.resolveError(res.message),
            life: 3000,
          });
        }
      },
      error: (err) => {
        this.loadingSave.set(false);
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant('label_failed'),
          detail: this.resolveError(err?.error?.message ?? 'label_error'),
          life: 3000,
        });
      },
    });
  }

  updateStatus(
    deliveryId: number,
    action: 'accept' | 'arrived' | 'pickup' | 'start' | 'complete',
    onSuccess?: () => void,
  ) {
    this.loadingSave.set(true);
    this.http
      .patch<ApiResponse<Delivery>>(`${this.baseUrl}/${deliveryId}/${action}`, {})
      .subscribe({
        next: (res) => {
          this.loadingSave.set(false);
          if (res.status) {
            this.delivery.set(res.data);
            this.messageService.add({
              severity: 'success',
              summary: this.translate.instant('label_successful'),
              detail: res.message,
              life: 3000,
            });
            onSuccess?.();
          } else {
            this.messageService.add({
              severity: 'error',
              summary: this.translate.instant('label_failed'),
              detail: this.resolveError(res.message),
              life: 3000,
            });
          }
        },
        error: (err) => {
          this.loadingSave.set(false);
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant('label_failed'),
            detail: this.resolveError(err?.error?.message ?? 'label_error'),
            life: 3000,
          });
        },
      });
  }

  cancelDelivery(deliveryId: number, req: CancelDeliveryRequest, onSuccess?: () => void) {
    this.loadingSave.set(true);
    this.http
      .patch<ApiResponse<Delivery>>(`${this.baseUrl}/${deliveryId}/cancel`, req)
      .subscribe({
        next: (res) => {
          this.loadingSave.set(false);
          if (res.status) {
            this.delivery.set(res.data);
            this.messageService.add({
              severity: 'success',
              summary: this.translate.instant('label_successful'),
              detail: res.message,
              life: 3000,
            });
            onSuccess?.();
          } else {
            this.messageService.add({
              severity: 'error',
              summary: this.translate.instant('label_failed'),
              detail: this.resolveError(res.message),
              life: 3000,
            });
          }
        },
        error: (err) => {
          this.loadingSave.set(false);
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant('label_failed'),
            detail: this.resolveError(err?.error?.message ?? 'label_error'),
            life: 3000,
          });
        },
      });
  }

  loadTrackingPoints(deliveryId: number) {
    this.http
      .get<ApiResponse<DeliveryTrackingPoint[]>>(`${this.baseUrl}/${deliveryId}/tracking`)
      .subscribe({
        next: (res) => {
          this.trackingPoints.set(res.data ?? []);
        },
        error: () => {},
      });
  }

  loadLatestPoint(deliveryId: number) {
    this.http
      .get<ApiResponse<DeliveryTrackingPoint>>(`${this.baseUrl}/${deliveryId}/tracking/latest`)
      .subscribe({
        next: (res) => {
          this.latestPoint.set(res.data ?? null);
        },
        error: () => {},
      });
  }
}
