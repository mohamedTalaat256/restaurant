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
import { CustomerTypeService } from './customer-type.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { CustomerType } from '../../../../core/model/customer-type.model';
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
  selector: 'app-customer-types',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './customer-types.html',
  styleUrls: ['./customer-types.scss'],
  providers: [MessageService, ConfirmationService, CustomerTypeService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CustomerTypes implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];

  customerTypeForm!: FormGroup;

  customerTypeService = inject(CustomerTypeService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'customer_types')?.id || 0;

  ngOnInit(): void {
    this.customerTypeService.loadCustomerTypes();
  }

  exportCSV() {
    this.dt.exportCSV();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.customerTypeService.customerTypeDialog.set(true);
  }

  editCustomerType(customerType: CustomerType) {
    this.customerTypeService.customerTypeDialog.set(true);
    this.setForm(customerType);
  }

  hideDialog() {
    this.customerTypeService.customerTypeDialog.set(false);
  }

  deleteCustomerType(customerType: CustomerType) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_customer_type'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.customerTypeService.deleteCustomerType(customerType.type);
      }
    });
  }

  saveCustomerType() {
    if (this.customerTypeForm.invalid) {
      this.customerTypeForm.markAllAsTouched();
      return;
    }

    if (this.customerTypeForm.get('type')?.value && this.customerTypeForm.get('type')?.disabled) {
      this.customerTypeService.updateCustomerType(this.customerTypeForm.getRawValue());
    } else {
      this.customerTypeService.saveCustomerType(this.customerTypeForm.value);
    }
  }

  initiatForm() {
    this.customerTypeForm = this.fb.group({
      type: ['', Validators.required],
      description: [''],
      ordering: [0, Validators.required],
      status: [true],
    });
  }

  setForm(customerType: CustomerType) {
    this.customerTypeForm = this.fb.group({
      type: [{value: customerType.type, disabled: true}, Validators.required],
      description: [customerType.description],
      ordering: [customerType.ordering, Validators.required],
      status: [customerType.status],
    });
  }
}
