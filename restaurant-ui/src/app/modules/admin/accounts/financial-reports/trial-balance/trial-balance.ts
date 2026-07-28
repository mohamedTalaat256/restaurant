import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { ProgressBarModule } from 'primeng/progressbar';
import { MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { FormInput } from '../../../../../shared/components/form-input/form-input';
import { TranslateService } from '../../../../../core/service/translate.service';
import { FinancialReportService } from '../financial-report.service';

@Component({
  selector: 'app-trial-balance',
  imports: [CommonModule, ButtonModule, TableModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, CardModule],
  templateUrl: './trial-balance.html',
  styleUrls: ['./trial-balance.scss'],
  providers: [MessageService, FinancialReportService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TrialBalanceComponent implements OnInit {

  reportService = inject(FinancialReportService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  filterForm!: FormGroup;

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.filterForm = this.fb.group({
      startDate: [null, [Validators.required]],
      endDate: [null, [Validators.required]],
    });
  }

  generateReport() {
    if (this.filterForm.invalid) {
      this.filterForm.markAllAsTouched();
      return;
    }

    const { startDate, endDate } = this.filterForm.value;
    const start = startDate instanceof Date ? startDate.toISOString() : startDate;
    const end = endDate instanceof Date ? endDate.toISOString() : endDate;

    this.reportService.loadTrialBalance(start, end);
  }
}
