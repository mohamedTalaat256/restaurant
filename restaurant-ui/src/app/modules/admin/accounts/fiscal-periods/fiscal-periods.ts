import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { ProgressBarModule } from 'primeng/progressbar';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { SelectModule } from 'primeng/select';
import { TranslateService } from '../../../../core/service/translate.service';
import { FiscalPeriodService } from './fiscal-period.service';
import { FiscalPeriod } from '../../../../core/model/fiscal-period.model';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-fiscal-periods',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule,TooltipModule,
    TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule, SelectModule,
    ReactiveFormsModule, Toast],
  templateUrl: './fiscal-periods.html',
  styleUrls: ['./fiscal-periods.scss'],
  providers: [MessageService, ConfirmationService, FiscalPeriodService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FiscalPeriods implements OnInit {

  @ViewChild('dt') dt!: Table;

  fiscalPeriodService = inject(FiscalPeriodService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  filterForm!: FormGroup;

  yearOptions: { label: string; value: number }[] = [];

  ngOnInit(): void {
    this.initYearOptions();
    this.initFilterForm();
    this.fiscalPeriodService.loadAllPeriods();
  }

  initYearOptions() {
    const currentYear = new Date().getFullYear();
    for (let y = currentYear - 5; y <= currentYear + 1; y++) {
      this.yearOptions.push({ label: y.toString(), value: y });
    }
  }

  initFilterForm() {
    this.filterForm = this.fb.group({
      year: [null],
    });
  }

  filterByYear() {
    const year = this.filterForm.value.year;
    if (year) {
      this.fiscalPeriodService.loadPeriodsByYear(year);
    } else {
      this.fiscalPeriodService.loadAllPeriods();
    }
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  lockPeriod(period: FiscalPeriod) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_lock_period'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.fiscalPeriodService.lockPeriod(period.year, period.month);
      }
    });
  }

  unlockPeriod(period: FiscalPeriod) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_unlock_period'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.fiscalPeriodService.unlockPeriod(period.year, period.month);
      }
    });
  }
}
