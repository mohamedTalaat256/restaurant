import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { ThirdPartyCustomer } from '../../../../core/model/third-party-customer.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class ThirdPartyCustomerService {

  thirdPartyCustomers = signal<ThirdPartyCustomer[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  thirdPartyCustomerDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadThirdPartyCustomers() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<ThirdPartyCustomer[]>>(env.apiUrl + '/settings/third-party-customers').subscribe({
      next: (res) => {
        this.thirdPartyCustomers.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveThirdPartyCustomer(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT;
    }

    this.http.post<any>(env.apiUrl + '/settings/third-party-customers', formValue).subscribe({
      next: (res: ApiResponse<ThirdPartyCustomer>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.thirdPartyCustomers.update((items) => [...items, res.data]);
          } else {
            this.thirdPartyCustomers.update((items) =>
              items.map(item => item.id === res.data.id ? res.data : item)
            );
          }

          this.loadingSave.set(false);
          this.thirdPartyCustomerDialog.set(false);
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

  updateThirdPartyCustomer(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/settings/third-party-customers/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<ThirdPartyCustomer>) => {
        if (res.status) {
          this.thirdPartyCustomers.update((items) =>
            items.map(item => item.id === res.data.id ? res.data : item)
          );
          this.loadingSave.set(false);
          this.thirdPartyCustomerDialog.set(false);
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

  deleteThirdPartyCustomer(id: number) {
    this.http.delete<any>(env.apiUrl + '/settings/third-party-customers/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.thirdPartyCustomers.update((items) => items.filter(item => item.id !== id));
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
