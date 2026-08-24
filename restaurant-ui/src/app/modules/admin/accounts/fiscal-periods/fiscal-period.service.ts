import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { FiscalPeriod } from '../../../../core/model/fiscal-period.model';
import { env } from '../../../../../environment/env';

@Injectable({ providedIn: 'root' })
export class FiscalPeriodService {

  fiscalPeriods = signal<FiscalPeriod[]>([]);
  loading = signal(false);
  loadingSave = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  private baseUrl = env.apiUrl + '/fiscal-periods';

  loadAllPeriods() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<FiscalPeriod[]>>(this.baseUrl).subscribe({
      next: (res) => {
        this.fiscalPeriods.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  loadPeriodsByYear(year: number) {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<FiscalPeriod[]>>(this.baseUrl + '/year/' + year).subscribe({
      next: (res) => {
        this.fiscalPeriods.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  lockPeriod(year: number, month: number) {
    this.loadingSave.set(true);
    this.error.set(null);

    const params = new HttpParams().set('year', year).set('month', month);

    this.http.post<ApiResponse<FiscalPeriod>>(this.baseUrl + '/lock', null, { params }).subscribe({
      next: (res) => {
        if (res.status) {
          this.fiscalPeriods.update((items) =>
            items.map(item => (item.year === year && item.month === month) ? res.data : item)
          );
          this.loadingSave.set(false);
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: res.message,
            life: 3000
          });
        } else {
          this.loadingSave.set(false);
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant('label_failed'),
            detail: res.message,
            life: 3000
          });
        }
      },
      error: () => {
        this.loadingSave.set(false);
      }
    });
  }

  unlockPeriod(year: number, month: number) {
    this.loadingSave.set(true);
    this.error.set(null);

    const params = new HttpParams().set('year', year).set('month', month);

    this.http.post<ApiResponse<FiscalPeriod>>(this.baseUrl + '/unlock', null, { params }).subscribe({
      next: (res) => {
        if (res.status) {
          this.fiscalPeriods.update((items) =>
            items.map(item => (item.year === year && item.month === month) ? res.data : item)
          );
          this.loadingSave.set(false);
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: res.message,
            life: 3000
          });
        } else {
          this.loadingSave.set(false);
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant('label_failed'),
            detail: res.message,
            life: 3000
          });
        }
      },
      error: () => {
        this.loadingSave.set(false);
      }
    });
  }
}
