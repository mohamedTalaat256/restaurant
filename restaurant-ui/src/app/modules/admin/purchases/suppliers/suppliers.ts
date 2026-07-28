import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { ProgressBarModule } from 'primeng/progressbar';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { SupplierService } from './supplier.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { Supplier } from '../../../../core/model/supplier.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { Toast } from 'primeng/toast';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';
import { ApplicationSettingService } from '../../settings/application-settings/application-setting.service';

@Component({
  selector: 'app-suppliers',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, TextareaModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './suppliers.html',
  styleUrls: ['./suppliers.scss'],
  providers: [MessageService, ConfirmationService, SupplierService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Suppliers implements OnInit {

  selectedSuppliers!: Supplier[] | null;
  @ViewChild('dt') dt!: Table;

  supplierForm!: FormGroup;

  supplierService = inject(SupplierService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  menuItemId: number = env.menuItems.find(item => item.name === 'suppliers')?.id || 0;
  readonly settingsService = inject(ApplicationSettingService);
  currencySymbol = this.settingsService.setting()?.currencySymbol || 'EGP';

  ngOnInit(): void {
    this.supplierService.loadSuppliers();
  }


  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.supplierService.supplierDialog.set(true);
  }

  editSupplier(supplier: Supplier) {
    this.supplierService.supplierDialog.set(true);
    this.setForm(supplier);
  }



  hideDialog() {
    this.supplierService.supplierDialog.set(false);
  }

  deleteSupplier(supplier: Supplier) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_supplier'),
      header: this.translate.instant('label_confrim'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.supplierService.deleteSupplier(supplier.id);
      }
    });
  }

  saveSupplier() {
    if (this.supplierForm.invalid) {
      this.supplierForm.markAllAsTouched();
      return;
    }

    if (this.supplierForm.value.id) {
      this.supplierService.updateSupplier(this.supplierForm.value);
    } else {
      this.supplierService.saveSupplier(this.supplierForm.value);
    }
  }

  initiatForm() {
    this.supplierForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      email: ['', Validators.email],
      phone: [''],
      address: [''],
      status: [true],
    });
  }

  setForm(supplier: Supplier) {
    this.supplierForm = this.fb.group({
      id: [supplier.id],
      name: [supplier.name, Validators.required],
      email: [supplier.email, Validators.email],
      phone: [supplier.phone],
      address: [supplier.address],
      status: [supplier.status],
    });
  }
}
