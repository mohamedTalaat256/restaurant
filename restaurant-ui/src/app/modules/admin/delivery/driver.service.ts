import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../core/service/translate.service';
import { ApiResponse } from '../../../core/model/api-response.model';
import { Driver, DriverStatus, ChangeDriverStatusRequest } from '../../../core/model/driver.model';
import { Delivery } from '../../../core/model/delivery.model';
import { env } from '../../../../environment/env';

@Injectable({ providedIn: 'root' })
export class DriverService {
  drivers = signal<Driver[]>([]);
  availableDrivers = signal<Driver[]>([]);
  driver = signal<Driver | null>(null);
  currentDelivery = signal<Delivery | null>(null);
  deliveryHistory = signal<Delivery[]>([]);

  loading = signal(false);
  loadingSave = signal(false);

  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  private baseUrl = env.apiUrl + '/drivers';

  loadDrivers() {
    this.loading.set(true);
    this.http.get<ApiResponse<Driver[]>>(this.baseUrl).subscribe({
      next: (res) => {
        this.drivers.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      },
    });
  }

  loadAvailableDrivers() {
    this.loading.set(true);
    this.http.get<ApiResponse<Driver[]>>(`${this.baseUrl}/available`).subscribe({
      next: (res) => {
        this.availableDrivers.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      },
    });
  }

  loadDriverById(id: number) {
    this.loading.set(true);
    this.http.get<ApiResponse<Driver>>(`${this.baseUrl}/${id}`).subscribe({
      next: (res) => {
        this.driver.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      },
    });
  }

  loadCurrentDelivery(driverId: number) {
    this.http.get<ApiResponse<Delivery>>(`${this.baseUrl}/${driverId}/current-delivery`).subscribe({
      next: (res) => {
        this.currentDelivery.set(res.data ?? null);
      },
      error: () => {
        this.currentDelivery.set(null);
      },
    });
  }

  loadDeliveryHistory(driverId: number) {
    this.http.get<ApiResponse<Delivery[]>>(`${this.baseUrl}/${driverId}/deliveries`).subscribe({
      next: (res) => {
        this.deliveryHistory.set(res.data ?? []);
      },
      error: () => {},
    });
  }

  changeStatus(driverId: number, req: ChangeDriverStatusRequest, onSuccess?: () => void) {
    this.loadingSave.set(true);
    this.http
      .patch<ApiResponse<Driver>>(`${this.baseUrl}/${driverId}/status`, req)
      .subscribe({
        next: (res) => {
          this.loadingSave.set(false);
          if (res.status) {
            this.drivers.update((list) =>
              list.map((d) => (d.id === driverId ? res.data : d)),
            );
            if (this.driver()?.id === driverId) {
              this.driver.set(res.data);
            }
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
              detail: res.message,
              life: 3000,
            });
          }
        },
        error: (err) => {
          this.loadingSave.set(false);
          const key = err?.error?.message ?? 'label_error';
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant('label_failed'),
            detail: this.translate.instant(key),
            life: 3000,
          });
        },
      });
  }
}
