import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { Language } from '../../../../core/model/language.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class LanguageService {

  languages = signal<Language[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  languageDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadLanguages() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<Language[]>>(env.apiUrl + '/settings/languages').subscribe({
      next: (res) => {
        this.languages.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveLanguage(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT;
    }

    this.http.post<any>(env.apiUrl + '/settings/languages', formValue).subscribe({
      next: (res: ApiResponse<Language>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.languages.update((languages) => [...languages, res.data]);
          } else {
            this.languages.update((languages) =>
              languages.map(l => l.languageCode === res.data.languageCode ? res.data : l)
            );
          }

          this.loadingSave.set(false);
          this.languageDialog.set(false);
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

  updateLanguage(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/settings/languages/' + formValue.languageCode, formValue).subscribe({
      next: (res: ApiResponse<Language>) => {
        if (res.status) {
          this.languages.update((languages) =>
            languages.map(l => l.languageCode === res.data.languageCode ? res.data : l)
          );
          this.loadingSave.set(false);
          this.languageDialog.set(false);
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

  deleteLanguage(languageCode: string) {
    this.http.delete<any>(env.apiUrl + '/settings/languages/' + languageCode).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.languages.update((languages) => languages.filter(l => l.languageCode !== languageCode));
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
