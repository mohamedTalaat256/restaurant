import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { ProgressBarModule } from 'primeng/progressbar';
import { MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { TagModule } from 'primeng/tag';
import { FormInput } from '../../../../../shared/components/form-input/form-input';
import { TranslateService } from '../../../../../core/service/translate.service';
import { FinancialReportService } from '../financial-report.service';

@Component({
  selector: 'app-balance-sheet',
  imports: [CommonModule, ButtonModule, TableModule,
    ProgressBarModule, DividerModule, TagModule,
    ReactiveFormsModule, FormInput, Toast, CardModule],
  templateUrl: './balance-sheet.html',
  styleUrls: ['./balance-sheet.scss'],
  providers: [MessageService, FinancialReportService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BalanceSheetComponent implements OnInit {

  reportService = inject(FinancialReportService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  filterForm!: FormGroup;

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.filterForm = this.fb.group({
      asOfDate: [null],
    });
  }

  generateReport() {
    const { asOfDate } = this.filterForm.value;
    const date = asOfDate instanceof Date ? asOfDate.toISOString() : asOfDate;
    this.reportService.loadBalanceSheet(date);
  }
}
