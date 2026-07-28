import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { Customer } from '../../../../core/model/customer.model';
import { CustomerType } from '../../../../core/model/customer-type.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class CustomerService {

  customers = signal<Customer[]>([]);
  customerTypes = signal<CustomerType[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  customerDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadCustomers() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<Customer[]>>(env.apiUrl + '/settings/customers').subscribe({
      next: (res) => {
        this.customers.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  loadCustomerTypes() {
    this.http.get<ApiResponse<CustomerType[]>>(env.apiUrl + '/settings/customer-types').subscribe({
      next: (res) => {
        this.customerTypes.set(res.data);
      },
      error: () => {
      }
    });
  }

  saveCustomer(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT;
    }

    this.http.post<any>(env.apiUrl + '/settings/customers', formValue).subscribe({
      next: (res: ApiResponse<Customer>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.customers.update((items) => [...items, res.data]);
          } else {
            this.customers.update((items) =>
              items.map(item => item.id === res.data.id ? res.data : item)
            );
          }

          this.loadingSave.set(false);
          this.customerDialog.set(false);
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

  updateCustomer(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/settings/customers/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<Customer>) => {
        if (res.status) {
          this.customers.update((items) =>
            items.map(item => item.id === res.data.id ? res.data : item)
          );
          this.loadingSave.set(false);
          this.customerDialog.set(false);
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

  deleteCustomer(id: number) {
    this.http.delete<any>(env.apiUrl + '/settings/customers/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.customers.update((items) => items.filter(item => item.id !== id));
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
