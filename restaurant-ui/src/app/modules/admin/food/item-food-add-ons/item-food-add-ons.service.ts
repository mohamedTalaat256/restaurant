import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { ItemFoodAddOns } from '../../../../core/model/item-food-add-ons.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class ItemFoodAddOnsService {

  itemFoodAddOns = signal<ItemFoodAddOns[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  itemFoodAddOnsDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadItemFoodAddOns() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<ItemFoodAddOns[]>>(env.apiUrl + '/food-management/item-food-add-ons').subscribe({
      next: (res) => {
        this.itemFoodAddOns.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveItemFoodAddOns(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT;
    }

    this.http.post<any>(env.apiUrl + '/food-management/item-food-add-ons', formValue).subscribe({
      next: (res: ApiResponse<ItemFoodAddOns>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.itemFoodAddOns.update((items) => [...items, res.data]);
          } else {
            this.itemFoodAddOns.update((items) =>
              items.map(item => item.id === res.data.id ? res.data : item)
            );
          }

          this.loadingSave.set(false);
          this.itemFoodAddOnsDialog.set(false);
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

  updateItemFoodAddOns(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/food-management/item-food-add-ons/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<ItemFoodAddOns>) => {
        if (res.status) {
          this.itemFoodAddOns.update((items) =>
            items.map(item => item.id === res.data.id ? res.data : item)
          );
          this.loadingSave.set(false);
          this.itemFoodAddOnsDialog.set(false);
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

  deleteItemFoodAddOns(id: number) {
    this.http.delete<any>(env.apiUrl + '/food-management/item-food-add-ons/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.itemFoodAddOns.update((items) => items.filter(item => item.id !== id));
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
