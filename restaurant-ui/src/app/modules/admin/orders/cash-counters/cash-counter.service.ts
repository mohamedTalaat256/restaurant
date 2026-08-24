import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { CashCounter } from '../../../../core/model/cash-counter.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class CashCounterService {

  cashCounters = signal<CashCounter[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  cashCounterDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadCashCounters() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<CashCounter[]>>(env.apiUrl + '/orders/cash/counters').subscribe({
      next: (res) => {
        this.cashCounters.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveCashCounter(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT;
    }

    this.http.post<any>(env.apiUrl + '/orders/cash/counters', formValue).subscribe({
      next: (res: ApiResponse<CashCounter>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.cashCounters.update((items) => [...items, res.data]);
          } else {
            this.cashCounters.update((items) =>
              items.map(item => item.id === res.data.id ? res.data : item)
            );
          }

          this.loadingSave.set(false);
          this.cashCounterDialog.set(false);
          this.savedSuccess.set(true);

          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: res.message,
            life: 3000
          });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
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

  updateCashCounter(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/orders/cash/counters/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<CashCounter>) => {
        if (res.status) {
          this.cashCounters.update((items) =>
            items.map(item => item.id === res.data.id ? res.data : item)
          );
          this.loadingSave.set(false);
          this.cashCounterDialog.set(false);
          this.savedSuccess.set(true);
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: res.message,
            life: 3000
          });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
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

  deleteCashCounter(id: number) {
    this.http.delete<any>(env.apiUrl + '/orders/cash/counters/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.cashCounters.update((items) => items.filter(item => item.id !== id));
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: res.message,
            life: 3000
          });
        }
      },
      error: () => {
      }
    });
  }
}
