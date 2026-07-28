import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { ProgressBarModule } from 'primeng/progressbar';
import { MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { SelectModule } from 'primeng/select';
import { FormInput } from '../../../../../shared/components/form-input/form-input';
import { TranslateService } from '../../../../../core/service/translate.service';
import { FinancialReportService } from '../financial-report.service';
import { AccountsService } from '../../accounts.service';

@Component({
  selector: 'app-general-ledger',
  imports: [CommonModule, ButtonModule, TableModule,
    ProgressBarModule, SelectModule,
    ReactiveFormsModule, FormInput, Toast, CardModule],
  templateUrl: './general-ledger.html',
  styleUrls: ['./general-ledger.scss'],
  providers: [MessageService, FinancialReportService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GeneralLedger implements OnInit {

  reportService = inject(FinancialReportService);
  accountsService = inject(AccountsService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  filterForm!: FormGroup;

  ngOnInit(): void {
    this.initForm();
    this.accountsService.loadAccountsFlat();
  }

  get accountOptions() {
    return this.accountsService.accountsFlat()
      .filter(a => a.allowTransaction)
      .map(a => ({ label: a.code + ' - ' + a.name, value: a.id }));
  }

  initForm() {
    this.filterForm = this.fb.group({
      accountId: [null, [Validators.required]],
      startDate: [null, [Validators.required]],
      endDate: [null, [Validators.required]],
    });
  }

  generateReport() {
    if (this.filterForm.invalid) {
      this.filterForm.markAllAsTouched();
      return;
    }

    const { accountId, startDate, endDate } = this.filterForm.value;
    const start = startDate instanceof Date ? startDate.toISOString() : startDate;
    const end = endDate instanceof Date ? endDate.toISOString() : endDate;

    this.reportService.loadGeneralLedger(accountId, start, end);
  }
}
