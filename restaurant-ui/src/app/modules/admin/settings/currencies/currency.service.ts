import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { Currency } from '../../../../core/model/currency.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class CurrencyService {

  currencies = signal<Currency[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  currencyDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadCurrencies() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<Currency[]>>(env.apiUrl + '/settings/currencies').subscribe({
      next: (res) => {
        this.currencies.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveCurrency(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT;
    }

    this.http.post<any>(env.apiUrl + '/settings/currencies', formValue).subscribe({
      next: (res: ApiResponse<Currency>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.currencies.update((currencies) => [...currencies, res.data]);
          } else {
            this.currencies.update((currencies) =>
              currencies.map(c => c.id === res.data.id ? res.data : c)
            );
          }

          this.loadingSave.set(false);
          this.currencyDialog.set(false);
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

  updateCurrency(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/settings/currencies/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<Currency>) => {
        if (res.status) {
          this.currencies.update((currencies) =>
            currencies.map(c => c.id === res.data.id ? res.data : c)
          );
          this.loadingSave.set(false);
          this.currencyDialog.set(false);
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

  deleteCurrency(id: number) {
    this.http.delete<any>(env.apiUrl + '/settings/currencies/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.currencies.update((currencies) => currencies.filter(c => c.id !== id));
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: this.translate.instant(res.message),
            life: 3000
          });
        }
      },
      error: () => {
      }
    });
  }
}
