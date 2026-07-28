import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { ProgressBarModule } from 'primeng/progressbar';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Toast } from "primeng/toast";
import { CurrencyService } from './currency.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { Currency } from '../../../../core/model/currency.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';

interface Column {
  field: string;
  header: string;
  customExportHeader?: string;
}

interface ExportColumn {
  title: string;
  dataKey: string;
}


@Component({
  selector: 'app-currencies',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './currencies.html',
  styleUrls: ['./currencies.scss'],
  providers: [MessageService, ConfirmationService, CurrencyService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class Currencies implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];

  currencyForm!: FormGroup;

  currencyService = inject(CurrencyService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'currencies')?.id || 0;

  ngOnInit(): void {
    this.currencyService.loadCurrencies();
  }

  exportCSV() {
    this.dt.exportCSV();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.currencyService.currencyDialog.set(true);
  }

  editCurrency(currency: Currency) {
    this.currencyService.currencyDialog.set(true);
    this.setForm(currency);
  }

  hideDialog() {
    this.currencyService.currencyDialog.set(false);
  }

  deleteCurrency(currency: Currency) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_currency'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.currencyService.deleteCurrency(currency.id);
      }
    });
  }

  saveCurrency() {
    if (this.currencyForm.invalid) {
      this.currencyForm.markAllAsTouched();
      return;
    }

    if (this.currencyForm.value.id) {
      this.currencyService.updateCurrency(this.currencyForm.value);
    } else {
      this.currencyService.saveCurrency(this.currencyForm.value);
    }
  }

  initiatForm() {
    this.currencyForm = this.fb.group({
      id: [null],
      code: ['', Validators.required],
      symbol: ['', Validators.required],
      name: ['', Validators.required],
      exchangeRateToUSD: [null, Validators.required],
    });
  }

  setForm(currency: Currency) {
    this.currencyForm = this.fb.group({
      id: [currency.id],
      code: [currency.code, Validators.required],
      symbol: [currency.symbol, Validators.required],
      name: [currency.name, Validators.required],
      exchangeRateToUSD: [currency.exchangeRateToUSD, Validators.required],
    });
  }
}
