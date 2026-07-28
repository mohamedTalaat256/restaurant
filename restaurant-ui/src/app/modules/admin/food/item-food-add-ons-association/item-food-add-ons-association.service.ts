import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { ItemFoodAddOnsAssociation } from '../../../../core/model/item-food-add-ons-association.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class ItemFoodAddOnsAssociationService {

  itemFoodAddOnsAssociations = signal<ItemFoodAddOnsAssociation[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  itemFoodAddOnsAssociationDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  saveItemFoodAddOnsAssociation(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT;
    }

    this.http.post<any>(env.apiUrl + '/food-management/item-food-add-ons-associations', formValue).subscribe({
      next: (res: ApiResponse<ItemFoodAddOnsAssociation>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.itemFoodAddOnsAssociations.update((items) => [...items, res.data]);
          } else {
            this.itemFoodAddOnsAssociations.update((items) =>
              items.map(item => item.id === res.data.id ? res.data : item)
            );
          }

          this.loadingSave.set(false);
          this.itemFoodAddOnsAssociationDialog.set(false);
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

  updateItemFoodAddOnsAssociation(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/food-management/item-food-add-ons-associations/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<ItemFoodAddOnsAssociation>) => {
        if (res.status) {
          this.itemFoodAddOnsAssociations.update((items) =>
            items.map(item => item.id === res.data.id ? res.data : item)
          );
          this.loadingSave.set(false);
          this.itemFoodAddOnsAssociationDialog.set(false);
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

  deleteItemFoodAddOnsAssociation(id: number) {
    this.http.delete<any>(env.apiUrl + '/food-management/item-food-add-ons-associations/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.itemFoodAddOnsAssociations.update((items) => items.filter(item => item.id !== id));
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
