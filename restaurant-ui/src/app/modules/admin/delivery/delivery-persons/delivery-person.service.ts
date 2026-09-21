import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { env } from '../../../../../environment/env';
import { TranslateService } from '../../../../core/service/translate.service';
import { DeliveryDetails } from '../../../../core/model/delivery-details.model';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class DeliveryPersonService {

  deliveries = signal<DeliveryDetails[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  deliveryDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  private readonly baseUrl = env.apiUrl + '/deliveries';

  loadDeliveries(status?: boolean | null) {
    this.loading.set(true);
    this.error.set(null);

    let params = new HttpParams();
    if (status !== undefined && status !== null) {
      params = params.set('status', status);
    }

    this.http.get<ApiResponse<DeliveryDetails[]>>(this.baseUrl, { params }).subscribe({
      next: (res) => {
        this.deliveries.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => { this.loading.set(false); }
    });
  }

  saveDelivery(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);
    let formMode = FormMode.CREATE;
    if (formValue.id) formMode = FormMode.EDIT;

    this.http.post<any>(this.baseUrl, formValue).subscribe({
      next: (res: ApiResponse<DeliveryDetails>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.deliveries.update((items) => [...items, res.data]);
          } else {
            this.deliveries.update((items) => items.map(item => item.id === res.data.id ? res.data : item));
          }
          this.loadingSave.set(false);
          this.deliveryDialog.set(false);
          this.savedSuccess.set(true);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: res.message, life: 3000 });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: res.message, life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  updateDelivery(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);
    this.http.put<any>(this.baseUrl + '/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<DeliveryDetails>) => {
        if (res.status) {
          this.deliveries.update((items) => items.map(item => item.id === res.data.id ? res.data : item));
          this.loadingSave.set(false);
          this.deliveryDialog.set(false);
          this.savedSuccess.set(true);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: res.message, life: 3000 });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: res.message, life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  updateStatus(id: number, status: boolean) {
    let params = new HttpParams().set('status', status);
    this.http.patch<any>(this.baseUrl + '/' + id + '/status', null, { params }).subscribe({
      next: (res: ApiResponse<DeliveryDetails>) => {
        if (res.status) {
          this.deliveries.update((items) => items.map(item => item.id === id ? { ...item, status } : item));
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: res.message, life: 3000 });
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: res.message, life: 3000 });
        }
      },
      error: () => {}
    });
  }

  deleteDelivery(id: number) {
    this.http.delete<any>(this.baseUrl + '/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.deliveries.update((items) => items.filter(item => item.id !== id));
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: res.message, life: 3000 });
        }
      },
      error: () => {}
    });
  }
}
