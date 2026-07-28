import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { ProgressBarModule } from 'primeng/progressbar';
import { MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { FormInput } from '../../../../../shared/components/form-input/form-input';
import { TranslateService } from '../../../../../core/service/translate.service';
import { FinancialReportService } from '../financial-report.service';

@Component({
  selector: 'app-profit-loss',
  imports: [CommonModule, ButtonModule, TableModule,
    ProgressBarModule, DividerModule,
    ReactiveFormsModule, FormInput, Toast, CardModule],
  templateUrl: './profit-loss.html',
  styleUrls: ['./profit-loss.scss'],
  providers: [MessageService, FinancialReportService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProfitLossComponent implements OnInit {

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

    this.reportService.loadProfitLoss(start, end);
  }
}
