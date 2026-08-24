import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../../../../core/model/user.model';
import { FormMode } from '../../../../core/enum/formModeEnum';
import { MessageService } from 'primeng/api';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { env } from '../../../../../environment/env';
import { TranslateService } from '../../../../core/service/translate.service';

@Injectable({ providedIn: 'root' })
export class UserService {


  users = signal<User[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  userDialog = signal(false);
  savedSuccess = signal(false);



  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  private toFormData(formValue: any, imageFile: File | null): FormData {
    const formData = new FormData();

    Object.keys(formValue).forEach((key) => {
      if (key === 'image') {
        return;
      }

      const value = formValue[key];
      if (value === null || value === undefined) {
        return;
      }

      if (Array.isArray(value)) {
        value.forEach((item) => formData.append(key, String(item)));
        return;
      }

      if (value instanceof Date) {
        formData.append(key, value.toISOString());
        return;
      }

      formData.append(key, String(value));
    });

    if (imageFile) {
      formData.append('imageFile', imageFile);
    }

    return formData;
  }

  loadUsers() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<User[]>>(env.apiUrl + '/users').subscribe({
      next: (res) => {
        this.users.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveUser(formValue: any, imageFile: File | null) {
    this.loadingSave.set(true);
    this.error.set(null);


    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT
    }

    const formData = this.toFormData(formValue, imageFile);


    this.http.post<any>(env.apiUrl + '/users', formData).subscribe({
      next: (res: ApiResponse<User>) => {

        if (res.status) {

          if (formMode === FormMode.CREATE) {
            this.users.update((users) => [...users, res.data]);
          } else {
            this.users.update((users) =>
              users.map(user => user.id === res.data.id ? res.data : user)
            );
          }

          this.loadingSave.set(false);
          this.userDialog.set(false);
          this.savedSuccess.set(true);

          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail:   res.message,
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

  updateUser(formValue: any, imageFile: File | null) {
    this.loadingSave.set(true);
    this.error.set(null);
    const formData = this.toFormData(formValue, imageFile);

    this.http.put<any>(env.apiUrl + '/users/' + formValue.id, formData).subscribe({
      next: (res: ApiResponse<User>) => {
        if (res.status) {

          this.users.update((users) =>
            users.map(user => user.id === res.data.id ? res.data : user)
          );
          this.loadingSave.set(false);
          this.userDialog.set(false);
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

  deleteUser(id: number) {
      this.http.delete<any>(env.apiUrl + '/users/' + id).subscribe({
        next: (res: ApiResponse<any>) => {
          if (res.status) {
            this.users.update((users) => users.filter(user => user.id !== id));
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
