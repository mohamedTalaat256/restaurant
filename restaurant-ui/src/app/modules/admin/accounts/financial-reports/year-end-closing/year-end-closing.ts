import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { ProgressBarModule } from 'primeng/progressbar';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { FormInput } from '../../../../../shared/components/form-input/form-input';
import { TranslateService } from '../../../../../core/service/translate.service';
import { FinancialReportService } from '../financial-report.service';

@Component({
  selector: 'app-year-end-closing',
  imports: [CommonModule, ButtonModule,
    ProgressBarModule, ConfirmDialogModule,
    ReactiveFormsModule, FormInput, Toast, CardModule],
  templateUrl: './year-end-closing.html',
  styleUrls: ['./year-end-closing.scss'],
  providers: [MessageService, ConfirmationService, FinancialReportService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class YearEndClosingComponent implements OnInit {

  reportService = inject(FinancialReportService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  form!: FormGroup;

  yearOptions: { label: string; value: number }[] = [];

  ngOnInit(): void {
    this.initForm();
    this.initYearOptions();
  }

  initYearOptions() {
    const currentYear = new Date().getFullYear();
    for (let y = currentYear - 5; y <= currentYear; y++) {
      this.yearOptions.push({ label: y.toString(), value: y });
    }
  }

  initForm() {
    this.form = this.fb.group({
      fiscalYear: [null, [Validators.required]],
    });
  }

  performClosing() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const year = this.form.value.fiscalYear;
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_year_end_closing'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.reportService.performYearEndClosing(year);
      }
    });
  }
}
