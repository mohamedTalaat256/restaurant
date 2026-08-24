import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import {
  GeneralLedgerReport,
  TrialBalance,
  ProfitLoss,
  BalanceSheet,
  YearEndClosingResult
} from '../../../../core/model/financial-report.model';
import { env } from '../../../../../environment/env';

@Injectable({ providedIn: 'root' })
export class FinancialReportService {

  generalLedgerReport = signal<GeneralLedgerReport | null>(null);
  trialBalance = signal<TrialBalance | null>(null);
  profitLoss = signal<ProfitLoss | null>(null);
  balanceSheet = signal<BalanceSheet | null>(null);
  yearEndClosingResult = signal<YearEndClosingResult | null>(null);

  loading = signal(false);
  loadingSave = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  private baseUrl = env.apiUrl + '/financial-reports';

  loadGeneralLedger(accountId: number, startDate: string, endDate: string) {
    this.loading.set(true);
    this.error.set(null);

    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);

    this.http.get<ApiResponse<GeneralLedgerReport>>(this.baseUrl + '/general-ledger/' + accountId, { params }).subscribe({
      next: (res) => {
        this.generalLedgerReport.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  loadTrialBalance(startDate: string, endDate: string) {
    this.loading.set(true);
    this.error.set(null);

    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);

    this.http.get<ApiResponse<TrialBalance>>(this.baseUrl + '/trial-balance', { params }).subscribe({
      next: (res) => {
        this.trialBalance.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  loadProfitLoss(startDate: string, endDate: string) {
    this.loading.set(true);
    this.error.set(null);

    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);

    this.http.get<ApiResponse<ProfitLoss>>(this.baseUrl + '/profit-loss', { params }).subscribe({
      next: (res) => {
        this.profitLoss.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  loadBalanceSheet(asOfDate?: string) {
    this.loading.set(true);
    this.error.set(null);

    let params = new HttpParams();
    if (asOfDate) {
      params = params.set('asOfDate', asOfDate);
    }

    this.http.get<ApiResponse<BalanceSheet>>(this.baseUrl + '/balance-sheet', { params }).subscribe({
      next: (res) => {
        this.balanceSheet.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  performYearEndClosing(fiscalYear: number) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.post<ApiResponse<YearEndClosingResult>>(this.baseUrl + '/year-end-closing/' + fiscalYear, null).subscribe({
      next: (res) => {
        if (res.status) {
          this.yearEndClosingResult.set(res.data);
          this.loadingSave.set(false);
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: res.message,
            life: 3000
          });
        } else {
          this.loadingSave.set(false);
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
