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
import { ThirdPartyCustomerService } from './third-party-customer.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { ThirdPartyCustomer } from '../../../../core/model/third-party-customer.model';
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
  selector: 'app-third-party-customers',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './third-party-customers.html',
  styleUrls: ['./third-party-customers.scss'],
  providers: [MessageService, ConfirmationService, ThirdPartyCustomerService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ThirdPartyCustomers implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];

  thirdPartyCustomerForm!: FormGroup;

  thirdPartyCustomerService = inject(ThirdPartyCustomerService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'third_party_customers')?.id || 0;

  ngOnInit(): void {
    this.thirdPartyCustomerService.loadThirdPartyCustomers();
  }

  exportCSV() {
    this.dt.exportCSV();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.thirdPartyCustomerService.thirdPartyCustomerDialog.set(true);
  }

  editThirdPartyCustomer(thirdPartyCustomer: ThirdPartyCustomer) {
    this.thirdPartyCustomerService.thirdPartyCustomerDialog.set(true);
    this.setForm(thirdPartyCustomer);
  }

  hideDialog() {
    this.thirdPartyCustomerService.thirdPartyCustomerDialog.set(false);
  }

  deleteThirdPartyCustomer(thirdPartyCustomer: ThirdPartyCustomer) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_third_party_customer'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.thirdPartyCustomerService.deleteThirdPartyCustomer(thirdPartyCustomer.id);
      }
    });
  }

  saveThirdPartyCustomer() {
    if (this.thirdPartyCustomerForm.invalid) {
      this.thirdPartyCustomerForm.markAllAsTouched();
      return;
    }

    if (this.thirdPartyCustomerForm.value.id) {
      this.thirdPartyCustomerService.updateThirdPartyCustomer(this.thirdPartyCustomerForm.value);
    } else {
      this.thirdPartyCustomerService.saveThirdPartyCustomer(this.thirdPartyCustomerForm.value);
    }
  }

  initiatForm() {
    this.thirdPartyCustomerForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      address: [''],
      phone: [''],
      email: [''],
      commissionPercentage: [0, Validators.required],
    });
  }

  setForm(thirdPartyCustomer: ThirdPartyCustomer) {
    this.thirdPartyCustomerForm = this.fb.group({
      id: [thirdPartyCustomer.id],
      name: [thirdPartyCustomer.name, Validators.required],
      address: [thirdPartyCustomer.address],
      phone: [thirdPartyCustomer.phone],
      email: [thirdPartyCustomer.email],
      commissionPercentage: [thirdPartyCustomer.commissionPercentage, Validators.required],
    });
  }
}
