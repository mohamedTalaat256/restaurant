import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, computed, inject, OnInit, ViewChild } from '@angular/core';
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
import { TableService } from './table.service';
import { FloorService } from '../floors/floor.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { RestaurantTable } from '../../../../core/model/restaurant-table.model';
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
  selector: 'app-tables',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './tables.html',
  styleUrls: ['./tables.scss'],
  providers: [MessageService, ConfirmationService, TableService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class Tables implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];

  tableForm!: FormGroup;

  tableService = inject(TableService);
  floorService = inject(FloorService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'tables')?.id || 0;

  floorOptions = computed(() => {
    return this.floorService.floors().map(floor => ({ label: floor.name, value: floor.id }));
  });

  ngOnInit(): void {
    this.tableService.loadTables();
    this.floorService.loadFloors();
  }

  exportCSV() {
    this.dt.exportCSV();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.tableService.tableDialog.set(true);
  }

  editTable(restaurantTable: RestaurantTable) {
    this.tableService.tableDialog.set(true);
    this.setForm(restaurantTable);
  }

  hideDialog() {
    this.tableService.tableDialog.set(false);
  }

  deleteTable(restaurantTable: RestaurantTable) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_table'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.tableService.deleteTable(restaurantTable.id);
      }
    });
  }

  saveTable() {
    if (this.tableForm.invalid) {
      this.tableForm.markAllAsTouched();
      return;
    }

    if (this.tableForm.value.id) {
      this.tableService.updateTable(this.tableForm.value);
    } else {
      this.tableService.saveTable(this.tableForm.value);
    }
  }

  initiatForm() {
    this.tableForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      capacity: [null, Validators.required],
      icon: [''],
      floorId: [null, Validators.required],
      status: [true],
    });
  }

  setForm(restaurantTable: RestaurantTable) {
    this.tableForm = this.fb.group({
      id: [restaurantTable.id],
      name: [restaurantTable.name, Validators.required],
      capacity: [restaurantTable.capacity, Validators.required],
      icon: [restaurantTable.icon],
      floorId: [restaurantTable.floor?.id || restaurantTable.floorId, Validators.required],
      status: [restaurantTable.status],
    });
  }
}
