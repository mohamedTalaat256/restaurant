import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { Uom } from '../../../../core/model/unit-of-measuremrnt.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class UomService {


  uoms = signal<Uom[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  uomDialog = signal(false);
  savedSuccess = signal(false);



  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadUoms() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<Uom[]>>(env.apiUrl + '/settings/unit-of-measurements').subscribe({
      next: (res) => {
        this.uoms.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveUom(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);


    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT
    }


    this.http.post<any>(env.apiUrl + '/settings/unit-of-measurements', formValue).subscribe({
      next: (res: ApiResponse<Uom>) => {

        if (res.status) {

          if (formMode === FormMode.CREATE) {
            this.uoms.update((uoms) => [...uoms, res.data]);
          } else {
            this.uoms.update((uoms) =>
              uoms.map(uom => uom.id === res.data.id ? res.data : uom)
            );
          }

          this.loadingSave.set(false);
          this.uomDialog.set(false);
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

  updateUom(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/settings/unit-of-measurements/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<Uom>) => {
        if (res.status) {

          this.uoms.update((uoms) =>
            uoms.map(uom => uom.id === res.data.id ? res.data : uom)
          );
          this.loadingSave.set(false);
          this.uomDialog.set(false);
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

  deleteUom(id: number) {
      this.http.delete<any>(env.apiUrl + '/settings/unit-of-measurements/' + id).subscribe({
        next: (res: ApiResponse<any>) => {
          if (res.status) {
            this.uoms.update((uoms) => uoms.filter(uom => uom.id !== id));
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
