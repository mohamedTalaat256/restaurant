import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { Kitchen } from '../../../../core/model/kitchen.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class KitchenService {

  kitchens = signal<Kitchen[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  kitchenDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadKitchens() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<Kitchen[]>>(env.apiUrl + '/settings/kitchens').subscribe({
      next: (res) => {
        this.kitchens.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveKitchen(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT;
    }

    this.http.post<any>(env.apiUrl + '/settings/kitchens', formValue).subscribe({
      next: (res: ApiResponse<Kitchen>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.kitchens.update((kitchens) => [...kitchens, res.data]);
          } else {
            this.kitchens.update((kitchens) =>
              kitchens.map(kitchen => kitchen.id === res.data.id ? res.data : kitchen)
            );
          }

          this.loadingSave.set(false);
          this.kitchenDialog.set(false);
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

  updateKitchen(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/settings/kitchens/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<Kitchen>) => {
        if (res.status) {
          this.kitchens.update((kitchens) =>
            kitchens.map(kitchen => kitchen.id === res.data.id ? res.data : kitchen)
          );
          this.loadingSave.set(false);
          this.kitchenDialog.set(false);
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

  deleteKitchen(id: number) {
    this.http.delete<any>(env.apiUrl + '/settings/kitchens/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.kitchens.update((kitchens) => kitchens.filter(kitchen => kitchen.id !== id));
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
