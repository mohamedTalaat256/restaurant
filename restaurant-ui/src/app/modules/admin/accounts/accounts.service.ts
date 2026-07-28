import { computed, inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { MessageService, TreeNode } from 'primeng/api';
import { TranslateService } from '../../../core/service/translate.service';
import { ApiResponse } from '../../../core/model/api-response.model';
import { env } from '../../../../environment/env';
import { Account } from '../../../core/model/account.model';
import { JournalEntryLine } from '../../../core/model/JournalEntryLine.model';
import { SupplierStatementReport } from '../../../core/model/supplier-report-item-dto.model';

@Injectable({ providedIn: 'root' })
export class AccountsService {

  accountsFlat = signal<Account[]>([]);
  accountsTree = signal<TreeNode[]>([]);
  accountTypes = signal<string[]>([]);
  transactionTypes = signal<string[]>([]);

  customerJournals = signal<JournalEntryLine[]>([]);
  supplierReport = signal<SupplierStatementReport | null>(null);
  supplierJournals = computed(() => this.supplierReport()?.items || []);

  loading = signal(false);
  loadingSave = signal(false);
  accountDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);

  loadAccountsFlat() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<Account[]>>(env.apiUrl + '/accounts/flat').subscribe({
      next: (res) => {
        this.accountsFlat.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  loadAccountsTree() {
    this.loading.set(true);
    this.error.set(null);
    this.http.get<ApiResponse<Account[]>>(env.apiUrl + '/accounts/tree').subscribe({
      next: (res) => {
        this.accountsTree.set(this.mapToPrimeNgTree(res.data));
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }


  loadAccountTypes() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<string[]>>(env.apiUrl + '/accounts/types').subscribe({
      next: (res) => {
        this.accountTypes.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }


  loadTransactionTypes() {
    this.loading.set(true);
    this.error.set(null);
    this.http.get<ApiResponse<string[]>>(env.apiUrl + '/accounts/transaction-types').subscribe({
      next: (res) => {
        this.transactionTypes.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  loadCustomerJournals(customerId: number) {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<JournalEntryLine[]>>(env.apiUrl + `/accounts/account-journals/customer-journals/${customerId}`).subscribe({
      next: (res) => {
        this.customerJournals.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }



  loadSupplierReport(supplierAccountId: number, fromDate: string, toDate: string) {
    this.loading.set(true);
    this.error.set(null);

    let params = new HttpParams();
    if (fromDate) params = params.set('fromDate', fromDate);
    if (toDate) params = params.set('toDate', toDate);

    this.http.get<ApiResponse<SupplierStatementReport>>(`${env.apiUrl}/accounts/${supplierAccountId}/statement`, { params }).subscribe({
      next: (res) => {
        this.supplierReport.set(res.data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }

  private mapToPrimeNgTree(accounts: Account[]): TreeNode[] {
    return accounts.map(acc => ({
      label: `${acc.code} - ${acc.name}`, // النص الذي سيظهر بالشجرة
      data: acc,                          // البيانات الأصلية للحساب بالكامل
      expanded: acc.code.length <= 2,     // جعل المستويات الرئيسية مفتوحة تلقائياً
      children: acc.children && acc.children.length > 0
        ? this.mapToPrimeNgTree(acc.children)
        : []
    }));
  }
}
