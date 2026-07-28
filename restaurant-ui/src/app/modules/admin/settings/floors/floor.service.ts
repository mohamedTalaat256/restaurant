import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { Floor } from '../../../../core/model/floor.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class FloorService {

  floors = signal<Floor[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  floorDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadFloors() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<Floor[]>>(env.apiUrl + '/settings/floors').subscribe({
      next: (res) => {
        this.floors.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveFloor(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT;
    }

    this.http.post<any>(env.apiUrl + '/settings/floors', formValue).subscribe({
      next: (res: ApiResponse<Floor>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.floors.update((floors) => [...floors, res.data]);
          } else {
            this.floors.update((floors) =>
              floors.map(floor => floor.id === res.data.id ? res.data : floor)
            );
          }

          this.loadingSave.set(false);
          this.floorDialog.set(false);
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

  updateFloor(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/settings/floors/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<Floor>) => {
        if (res.status) {
          this.floors.update((floors) =>
            floors.map(floor => floor.id === res.data.id ? res.data : floor)
          );
          this.loadingSave.set(false);
          this.floorDialog.set(false);
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

  deleteFloor(id: number) {
    this.http.delete<any>(env.apiUrl + '/settings/floors/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.floors.update((floors) => floors.filter(floor => floor.id !== id));
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
