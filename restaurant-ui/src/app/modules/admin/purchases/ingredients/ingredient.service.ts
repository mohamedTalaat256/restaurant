import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { env } from '../../../../../environment/env';
import { TranslateService } from '../../../../core/service/translate.service';
import { Ingredient } from '../../../../core/model/ingredient.model';
import { FormMode } from '../../../../core/enum/formModeEnum';

@Injectable({ providedIn: 'root' })
export class IngredientService {

  ingredients = signal<Ingredient[]>([]);
  loading = signal(false);
  loadingSave = signal(false);
  ingredientDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadIngredients() {
    this.loading.set(true);
    this.error.set(null);
    this.http.get<ApiResponse<Ingredient[]>>(env.apiUrl + '/purchase/ingredients').subscribe({
      next: (res) => {
        this.ingredients.set(res.data);
        this.loading.set(false);
      },
      error: () => { this.loading.set(false); }
    });
  }

  saveIngredient(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);
    let formMode = FormMode.CREATE;
    if (formValue.id) formMode = FormMode.EDIT;

    this.http.post<any>(env.apiUrl + '/purchase/ingredients', formValue).subscribe({
      next: (res: ApiResponse<Ingredient>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.ingredients.update((items) => [...items, res.data]);
          } else {
            this.ingredients.update((items) => items.map(item => item.id === res.data.id ? res.data : item));
          }
          this.loadingSave.set(false);
          this.ingredientDialog.set(false);
          this.savedSuccess.set(true);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: res.message, life: 3000 });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: res.message, life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  updateIngredient(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);
    this.http.put<any>(env.apiUrl + '/purchase/ingredients/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<Ingredient>) => {
        if (res.status) {
          this.ingredients.update((items) => items.map(item => item.id === res.data.id ? res.data : item));
          this.loadingSave.set(false);
          this.ingredientDialog.set(false);
          this.savedSuccess.set(true);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: res.message, life: 3000 });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: res.message, life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  deleteIngredient(id: number) {
    this.http.delete<any>(env.apiUrl + '/purchase/ingredients/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.ingredients.update((items) => items.filter(item => item.id !== id));
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: res.message, life: 3000 });
        }
      },
      error: () => {}
    });
  }
}
