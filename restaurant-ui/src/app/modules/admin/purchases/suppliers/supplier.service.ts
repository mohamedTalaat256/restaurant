import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { env } from '../../../../../environment/env';
import { TranslateService } from '../../../../core/service/translate.service';
import { Supplier } from '../../../../core/model/supplier.model';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class SupplierService {

  suppliers = signal<Supplier[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  supplierDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadSuppliers() {
    this.loading.set(true);
    this.error.set(null);
    this.http.get<ApiResponse<Supplier[]>>(env.apiUrl + '/purchase/suppliers').subscribe({
      next: (res) => {
        this.suppliers.set(res.data);
        this.loading.set(false);
      },
      error: () => { this.loading.set(false); }
    });
  }

  saveSupplier(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);
    let formMode = FormMode.CREATE;
    if (formValue.id) formMode = FormMode.EDIT;

    this.http.post<any>(env.apiUrl + '/purchase/suppliers', formValue).subscribe({
      next: (res: ApiResponse<Supplier>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.suppliers.update((items) => [...items, res.data]);
          } else {
            this.suppliers.update((items) => items.map(item => item.id === res.data.id ? res.data : item));
          }
          this.loadingSave.set(false);
          this.supplierDialog.set(false);
          this.savedSuccess.set(true);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  updateSupplier(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);
    this.http.put<any>(env.apiUrl + '/purchase/suppliers/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<Supplier>) => {
        if (res.status) {
          this.suppliers.update((items) => items.map(item => item.id === res.data.id ? res.data : item));
          this.loadingSave.set(false);
          this.supplierDialog.set(false);
          this.savedSuccess.set(true);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  deleteSupplier(id: number) {
    this.http.delete<any>(env.apiUrl + '/purchase/suppliers/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.suppliers.update((items) => items.filter(item => item.id !== id));
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => {}
    });
  }
}
