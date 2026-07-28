import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { JournalEntry, ManualJournalEntryRequest } from '../../../../core/model/journal-entry.model';
import { env } from '../../../../../environment/env';
import { FinancialTransactionRequest } from '../../../../core/model/financial-transaction-request.model';

@Injectable({ providedIn: 'root' })
export class JournalEntryService {

  loading = signal(false);
  loadingSave = signal(false);
  journalDialog = signal(false);
  savedSuccess = signal(false);
  lastCreatedEntry = signal<JournalEntry | null>(null);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  private baseUrl = env.apiUrl + '/journal-entries';

  createManualEntry(request: ManualJournalEntryRequest) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.post<ApiResponse<JournalEntry>>(this.baseUrl + '/manual', request).subscribe({
      next: (res) => {
        if (res.status) {
          this.lastCreatedEntry.set(res.data);
          this.loadingSave.set(false);
          this.journalDialog.set(false);
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

  post(request: FinancialTransactionRequest) {
    this.loadingSave.set(true);
    this.error.set(null);
    this.http.post<ApiResponse<JournalEntry>>(this.baseUrl + '/post', request).subscribe({
      next: (res) => {
        if (res.status) {
          this.lastCreatedEntry.set(res.data);
          this.loadingSave.set(false);
          this.journalDialog.set(false);
          this.savedSuccess.set(true);
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: this.translate.instant(res.message),
            life: 3000
          });
        }
        else {
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
}
