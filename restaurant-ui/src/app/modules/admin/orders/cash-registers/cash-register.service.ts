import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { CashRegister, OpenCashRegisterDto, CloseCashRegisterDto } from '../../../../core/model/cash-register.model';
import { env } from '../../../../../environment/env';

@Injectable({ providedIn: 'root' })
export class CashRegisterService {

  cashRegisters = signal<CashRegister[]>([]);
  currentOpenRegister = signal<CashRegister | null>(null);
  loading = signal(false);
  loadingSave = signal(false);
  cashRegisterDialog = signal(false);
  closeDialog = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  private baseUrl = env.apiUrl + '/orders/cash/registers';

  loadCashRegisters() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<CashRegister[]>>(this.baseUrl).subscribe({
      next: (res) => {
        this.cashRegisters.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  getMyOpenCashRegister() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<CashRegister>>(this.baseUrl + '/my-open').subscribe({
      next: (res) => {
        this.currentOpenRegister.set(res.data ?? null);
        this.loading.set(false);
      },
      error: () => {
        this.currentOpenRegister.set(null);
        this.loading.set(false);
      }
    });
  }

  openCashRegister(dto: OpenCashRegisterDto) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.post<ApiResponse<CashRegister>>(this.baseUrl + '/open', dto).subscribe({
      next: (res) => {
        if (res.status) {
          this.currentOpenRegister.set(res.data);
          this.cashRegisters.update((items) => [...items, res.data]);
          this.loadingSave.set(false);
          this.cashRegisterDialog.set(false);

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

  closeCashRegister(dto: CloseCashRegisterDto) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.post<ApiResponse<CashRegister>>(this.baseUrl + '/close', dto).subscribe({
      next: (res) => {
        if (res.status) {
          this.currentOpenRegister.set(null);
          this.cashRegisters.update((items) =>
            items.map(item => item.id === res.data.id ? res.data : item)
          );
          this.loadingSave.set(false);
          this.closeDialog.set(false);

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
}
