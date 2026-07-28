import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, OnInit, ViewChild } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
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
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';
import { AccountsService } from '../accounts.service';
import { ActivatedRoute } from '@angular/router';
import { SupplierService } from '../../purchases/suppliers/supplier.service';
import { DatePickerModule } from 'primeng/datepicker';

@Component({
  selector: 'app-supplier-account-report',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,DatePickerModule,
    FormsModule,
    SelectModule,
    ReactiveFormsModule, Toast],
  templateUrl: './supplier-account-report.html',
  styleUrls: ['./supplier-account-report.scss'],
  providers: [ConfirmationService, AccountsService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class SupplierAccountReport implements OnInit {
  @ViewChild('dt') dt!: Table;

  accountService = inject(AccountsService);
  supplierService = inject(SupplierService);
  readonly translate = inject(TranslateService);

  selectedSupplier: { label: string, value: number } | null = null;
  fromDate: Date = new Date(new Date().getFullYear(), new Date().getMonth(), 1);
  toDate: Date = new Date();

  currencyName = JSON.parse(localStorage.getItem('applicationSettings')!)?.currencyName || '';

  supplierOptions = computed(() => {
    return this.supplierService.suppliers().map(supplier => ({ label: supplier.name, value: supplier.accountId }));
  });

  ngOnInit(): void {
    this.supplierService.loadSuppliers();
  }

  onFilterChange() {
    if (this.selectedSupplier?.value) {
      const fromStr = this.fromDate ? this.fromDate.toISOString() : '';
      const toStr = this.toDate ? this.toDate.toISOString() : '';

      this.accountService.loadSupplierReport(this.selectedSupplier.value, fromStr, toStr);
    }
  }

}
