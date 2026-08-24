import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { CustomerType } from '../../../../core/model/customer-type.model';
import { env } from '../../../../../environment/env';

@Injectable({ providedIn: 'root' })
export class CustomerTypeService {

  customerTypes = signal<CustomerType[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  customerTypeDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadCustomerTypes() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<CustomerType[]>>(env.apiUrl + '/settings/customer-types').subscribe({
      next: (res) => {
        this.customerTypes.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveCustomerType(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.post<any>(env.apiUrl + '/settings/customer-types', formValue).subscribe({
      next: (res: ApiResponse<CustomerType>) => {
        if (res.status) {
          this.customerTypes.update((items) => {
            const index = items.findIndex(i => i.type === res.data.type);
            if (index >= 0) {
              return items.map(item => item.type === res.data.type ? res.data : item);
            }
            return [...items, res.data];
          });

          this.loadingSave.set(false);
          this.customerTypeDialog.set(false);
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

  updateCustomerType(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/settings/customer-types/' + formValue.type, formValue).subscribe({
      next: (res: ApiResponse<CustomerType>) => {
        if (res.status) {
          this.customerTypes.update((items) =>
            items.map(item => item.type === res.data.type ? res.data : item)
          );
          this.loadingSave.set(false);
          this.customerTypeDialog.set(false);
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

  deleteCustomerType(type: string) {
    this.http.delete<any>(env.apiUrl + '/settings/customer-types/' + type).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.customerTypes.update((items) => items.filter(item => item.type !== type));
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
