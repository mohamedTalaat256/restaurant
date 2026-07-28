import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { MenuType } from '../../../../core/model/menu-type.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class MenuTypeService {

  menuTypes = signal<MenuType[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  menuTypeDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadMenuTypes() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<MenuType[]>>(env.apiUrl + '/food-management/menu-types').subscribe({
      next: (res) => {
        this.menuTypes.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveMenuType(formValue: any, imageFile: File | null) {
    this.loadingSave.set(true);
    this.error.set(null);

    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT;
    }

    const formData = this.toFormData(formValue, imageFile);

    this.http.post<any>(env.apiUrl + '/food-management/menu-types', formData).subscribe({
      next: (res: ApiResponse<MenuType>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.menuTypes.update((items) => [...items, res.data]);
          } else {
            this.menuTypes.update((items) =>
              items.map(item => item.id === res.data.id ? res.data : item)
            );
          }

          this.loadingSave.set(false);
          this.menuTypeDialog.set(false);
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

  updateMenuType(formValue: any, imageFile: File | null) {
    this.loadingSave.set(true);
    this.error.set(null);

    const formData = this.toFormData(formValue, imageFile);

    this.http.put<any>(env.apiUrl + '/food-management/menu-types/' + formValue.id, formData).subscribe({
      next: (res: ApiResponse<MenuType>) => {
        if (res.status) {
          this.menuTypes.update((items) =>
            items.map(item => item.id === res.data.id ? res.data : item)
          );
          this.loadingSave.set(false);
          this.menuTypeDialog.set(false);
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

  deleteMenuType(id: number) {
    this.http.delete<any>(env.apiUrl + '/food-management/menu-types/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.menuTypes.update((items) => items.filter(item => item.id !== id));
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

  private toFormData(formValue: any, imageFile: File | null): FormData {
    const formData = new FormData();

    Object.keys(formValue).forEach((key) => {
      if (key === 'image') return;
      const value = formValue[key];
      if (value === null || value === undefined) return;
      formData.append(key, String(value));
    });

    if (imageFile) {
      formData.append('imageFile', imageFile);
    }

    return formData;
  }
}
