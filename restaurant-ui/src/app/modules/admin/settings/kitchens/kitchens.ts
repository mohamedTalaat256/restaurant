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
import { KitchenService } from './kitchen.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { Kitchen } from '../../../../core/model/kitchen.model';
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
  selector: 'app-kitchens',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './kitchens.html',
  styleUrls: ['./kitchens.scss'],
  providers: [MessageService, ConfirmationService, KitchenService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class Kitchens implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];

  kitchenForm!: FormGroup;

  kitchenService = inject(KitchenService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'kitchens')?.id || 0;

  ngOnInit(): void {
    this.kitchenService.loadKitchens();
  }

  exportCSV() {
    this.dt.exportCSV();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.kitchenService.kitchenDialog.set(true);
  }

  editKitchen(kitchen: Kitchen) {
    this.kitchenService.kitchenDialog.set(true);
    this.setForm(kitchen);
  }

  hideDialog() {
    this.kitchenService.kitchenDialog.set(false);
  }

  deleteKitchen(kitchen: Kitchen) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_kitchen'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.kitchenService.deleteKitchen(kitchen.id);
      }
    });
  }

  saveKitchen() {
    if (this.kitchenForm.invalid) {
      this.kitchenForm.markAllAsTouched();
      return;
    }

    if (this.kitchenForm.value.id) {
      this.kitchenService.updateKitchen(this.kitchenForm.value);
    } else {
      this.kitchenService.saveKitchen(this.kitchenForm.value);
    }
  }

  initiatForm() {
    this.kitchenForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      ipAddress: [''],
      port: [null],
      status: [true],
    });
  }

  setForm(kitchen: Kitchen) {
    this.kitchenForm = this.fb.group({
      id: [kitchen.id],
      name: [kitchen.name, Validators.required],
      ipAddress: [kitchen.ipAddress],
      port: [kitchen.port],
      status: [kitchen.status],
    });
  }
}
