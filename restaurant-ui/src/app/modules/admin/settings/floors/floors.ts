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
import { FloorService } from './floor.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { Floor } from '../../../../core/model/floor.model';
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
  selector: 'app-floors',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './floors.html',
  styleUrls: ['./floors.scss'],
  providers: [MessageService, ConfirmationService, FloorService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class Floors implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];

  floorForm!: FormGroup;

  floorService = inject(FloorService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'floors')?.id || 0;

  ngOnInit(): void {
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
    this.floorService.floorDialog.set(true);
  }

  editFloor(floor: Floor) {
    this.floorService.floorDialog.set(true);
    this.setForm(floor);
  }

  hideDialog() {
    this.floorService.floorDialog.set(false);
  }

  deleteFloor(floor: Floor) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_floor'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.floorService.deleteFloor(floor.id);
      }
    });
  }

  saveFloor() {
    if (this.floorForm.invalid) {
      this.floorForm.markAllAsTouched();
      return;
    }

    if (this.floorForm.value.id) {
      this.floorService.updateFloor(this.floorForm.value);
    } else {
      this.floorService.saveFloor(this.floorForm.value);
    }
  }

  initiatForm() {
    this.floorForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
    });
  }

  setForm(floor: Floor) {
    this.floorForm = this.fb.group({
      id: [floor.id],
      name: [floor.name, Validators.required],
    });
  }
}
