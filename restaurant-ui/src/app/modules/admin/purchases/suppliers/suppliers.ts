import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit, ViewChild } from '@angular/core';
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
import { SupplierService } from './supplier.service';
import { Supplier } from '../../../../core/model/supplier.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { Toast } from 'primeng/toast';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';
import { ApplicationSettingService } from '../../settings/application-settings/application-setting.service';
import { SupplierFormDialog } from './supplier-form-dialog/supplier-form-dialog';

@Component({
  selector: 'app-suppliers',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule,
    TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective, SupplierFormDialog],
  templateUrl: './suppliers.html',
  styleUrls: ['./suppliers.scss'],
  providers: [MessageService, ConfirmationService, SupplierService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Suppliers implements OnInit {

  selectedSuppliers!: Supplier[] | null;
  @ViewChild('dt') dt!: Table;

  supplierDialogVisible = false;
  editingSupplier: Supplier | null = null;

  supplierService = inject(SupplierService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);

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
    this.editingSupplier = null;
    this.supplierDialogVisible = true;
  }

  editSupplier(supplier: Supplier) {
    this.editingSupplier = supplier;
    this.supplierDialogVisible = true;
  }

  onDialogVisibleChange(visible: boolean) {
    this.supplierDialogVisible = visible;
    if (!visible) {
      this.editingSupplier = null;
    }
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

  onSupplierSaved() {
    this.supplierService.loadSuppliers();
  }
}
