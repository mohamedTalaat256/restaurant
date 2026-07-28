import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { CostCenter } from '../../../../core/model/cost-center.model';
import { env } from '../../../../../environment/env';

@Injectable({ providedIn: 'root' })
export class CostCenterService {

  costCenters = signal<CostCenter[]>([]);
  activeCostCenters = signal<CostCenter[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  costCenterDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  private baseUrl = env.apiUrl + '/cost-centers';

  loadCostCenters() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<CostCenter[]>>(this.baseUrl).subscribe({
      next: (res) => {
        this.costCenters.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  loadActiveCostCenters() {
    this.http.get<ApiResponse<CostCenter[]>>(this.baseUrl + '/active').subscribe({
      next: (res) => {
        this.activeCostCenters.set(res.data);
      }
    });
  }

  saveCostCenter(formValue: CostCenter) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.post<ApiResponse<CostCenter>>(this.baseUrl, formValue).subscribe({
      next: (res) => {
        if (res.status) {
          this.costCenters.update((items) => [...items, res.data]);
          this.loadingSave.set(false);
          this.costCenterDialog.set(false);
          this.savedSuccess.set(true);

          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: this.translate.instant(res.message),
            life: 3000
          });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant('label_failed'),
            detail: this.translate.instant(res.message),
            life: 3000
          });
        }
      },
      error: () => {
        this.loadingSave.set(false);
      }
    });
  }

  updateCostCenter(formValue: CostCenter) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<ApiResponse<CostCenter>>(this.baseUrl + '/' + formValue.id, formValue).subscribe({
      next: (res) => {
        if (res.status) {
          this.costCenters.update((items) =>
            items.map(item => item.id === res.data.id ? res.data : item)
          );
          this.loadingSave.set(false);
          this.costCenterDialog.set(false);
          this.savedSuccess.set(true);
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: this.translate.instant(res.message),
            life: 3000
          });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant('label_failed'),
            detail: this.translate.instant(res.message),
            life: 3000
          });
        }
      },
      error: () => {
        this.loadingSave.set(false);
      }
    });
  }

  deleteCostCenter(id: number) {
    this.http.delete<ApiResponse<any>>(this.baseUrl + '/' + id).subscribe({
      next: (res) => {
        if (res.status) {
          this.costCenters.update((items) => items.filter(item => item.id !== id));
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: this.translate.instant(res.message),
            life: 3000
          });
        }
      }
    });
  }
}
