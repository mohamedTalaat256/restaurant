import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { ProgressBarModule } from 'primeng/progressbar';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Toast } from "primeng/toast";
import { CashCounterService } from './cash-counter.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { CashCounter } from '../../../../core/model/cash-counter.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';

@Component({
  selector: 'app-cash-counters',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './cash-counters.html',
  styleUrls: ['./cash-counters.scss'],
  providers: [MessageService, ConfirmationService, CashCounterService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CashCounters implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;

  cashCounterForm!: FormGroup;

  cashCounterService = inject(CashCounterService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'counters')?.id || 0;

  ngOnInit(): void {
    this.cashCounterService.loadCashCounters();
  }

  exportCSV() {
    this.dt.exportCSV();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.cashCounterService.cashCounterDialog.set(true);
  }

  editCashCounter(cashCounter: CashCounter) {
    this.cashCounterService.cashCounterDialog.set(true);
    this.setForm(cashCounter);
  }

  hideDialog() {
    this.cashCounterService.cashCounterDialog.set(false);
  }

  deleteCashCounter(cashCounter: CashCounter) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_cash_counter'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.cashCounterService.deleteCashCounter(cashCounter.id);
      }
    });
  }

  saveCashCounter() {
    if (this.cashCounterForm.invalid) {
      this.cashCounterForm.markAllAsTouched();
      return;
    }

    if (this.cashCounterForm.value.id) {
      this.cashCounterService.updateCashCounter(this.cashCounterForm.value);
    } else {
      this.cashCounterService.saveCashCounter(this.cashCounterForm.value);
    }
  }

  initiatForm() {
    this.cashCounterForm = this.fb.group({
      id: [null],
      number: [null, [Validators.required]],
    });
  }

  setForm(cashCounter: CashCounter) {
    this.cashCounterForm = this.fb.group({
      id: [cashCounter.id],
      number: [cashCounter.number, [Validators.required]],
    });
  }
}
