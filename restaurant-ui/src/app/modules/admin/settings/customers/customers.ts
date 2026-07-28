import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, computed, inject, OnInit, signal, ViewChild } from '@angular/core';
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
import { CustomerService } from './customer.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { Customer } from '../../../../core/model/customer.model';
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
  selector: 'app-customers',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './customers.html',
  styleUrls: ['./customers.scss'],
  providers: [MessageService, ConfirmationService, CustomerService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Customers implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];

  customerForm!: FormGroup;

  customerService = inject(CustomerService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  customerTypesOptions = computed(() => {
    return this.customerService.customerTypes().map(type => ({ label: type.description, value: type.type }));
  });

  menuItemId: number = env.menuItems.find(item => item.name === 'customers')?.id || 0;

  ngOnInit(): void {
    this.customerService.loadCustomers();
    this.customerService.loadCustomerTypes();
  }

  exportCSV() {
    this.dt.exportCSV();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.customerService.customerDialog.set(true);
  }

  editCustomer(customer: Customer) {
    this.customerService.customerDialog.set(true);
    this.setForm(customer);
  }

  hideDialog() {
    this.customerService.customerDialog.set(false);
  }

  deleteCustomer(customer: Customer) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_customer'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.customerService.deleteCustomer(customer.id);
      }
    });
  }

  saveCustomer() {
    if (this.customerForm.invalid) {
      this.customerForm.markAllAsTouched();
      return;
    }

    if (this.customerForm.value.id) {
      this.customerService.updateCustomer(this.customerForm.value);
    } else {
      this.customerService.saveCustomer(this.customerForm.value);
    }
  }

  initiatForm() {
    this.customerForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      address: [''],
      favoriteDeliveryAddress: [''],
      password: [''],
      customerType: [null, Validators.required],
      allowCredit: [false],
      status: [true],
    });
  }

  setForm(customer: Customer) {
    this.customerForm = this.fb.group({
      id: [customer.id],
      name: [customer.name, Validators.required],
      email: [customer.email, [Validators.required, Validators.email]],
      phone: [customer.phone, Validators.required],
      address: [customer.address],
      favoriteDeliveryAddress: [customer.favoriteDeliveryAddress],
      password: [''],
      customerType: [customer.customerType?.type, Validators.required],
      allowCredit: [customer.allowCredit],
      status: [customer.status],
    });
  }
}
