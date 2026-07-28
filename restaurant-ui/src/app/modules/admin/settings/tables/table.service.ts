import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { RestaurantTable } from '../../../../core/model/restaurant-table.model';
import { env } from '../../../../../environment/env';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class TableService {

  tables = signal<RestaurantTable[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  tableDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadTables() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<RestaurantTable[]>>(env.apiUrl + '/settings/tables').subscribe({
      next: (res) => {
        this.tables.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveTable(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    let formMode = FormMode.CREATE;
    if (formValue.id) {
      formMode = FormMode.EDIT;
    }

    this.http.post<any>(env.apiUrl + '/settings/tables', formValue).subscribe({
      next: (res: ApiResponse<RestaurantTable>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.tables.update((tables) => [...tables, res.data]);
          } else {
            this.tables.update((tables) =>
              tables.map(table => table.id === res.data.id ? res.data : table)
            );
          }

          this.loadingSave.set(false);
          this.tableDialog.set(false);
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

  updateTable(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.put<any>(env.apiUrl + '/settings/tables/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<RestaurantTable>) => {
        if (res.status) {
          this.tables.update((tables) =>
            tables.map(table => table.id === res.data.id ? res.data : table)
          );
          this.loadingSave.set(false);
          this.tableDialog.set(false);
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

  deleteTable(id: number) {
    this.http.delete<any>(env.apiUrl + '/settings/tables/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.tables.update((tables) => tables.filter(table => table.id !== id));
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
