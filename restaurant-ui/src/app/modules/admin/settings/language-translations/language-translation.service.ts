import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { LanguageTranslation } from '../../../../core/model/language-translation.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class LanguageTranslationService {

  translations = signal<LanguageTranslation[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  translationDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadTranslations() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<LanguageTranslation[]>>(env.apiUrl + '/settings/language-translations').subscribe({
      next: (res) => {
        this.translations.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveTranslation(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT;
    }

    this.http.post<any>(env.apiUrl + '/settings/language-translations', formValue).subscribe({
      next: (res: ApiResponse<LanguageTranslation>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.translations.update((translations) => [...translations, res.data]);
          } else {
            this.translations.update((translations) =>
              translations.map(t => t.id === res.data.id ? res.data : t)
            );
          }

          this.loadingSave.set(false);
          this.translationDialog.set(false);
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

  updateTranslation(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/settings/language-translations/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<LanguageTranslation>) => {
        if (res.status) {
          this.translations.update((translations) =>
            translations.map(t => t.id === res.data.id ? res.data : t)
          );
          this.loadingSave.set(false);
          this.translationDialog.set(false);
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

  deleteTranslation(id: number) {
    this.http.delete<any>(env.apiUrl + '/settings/language-translations/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.translations.update((translations) => translations.filter(t => t.id !== id));
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
