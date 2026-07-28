import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, computed, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { RatingModule } from 'primeng/rating';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { ProgressBarModule } from 'primeng/progressbar';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { AvatarModule } from 'primeng/avatar';
import { Toast } from "primeng/toast";
import { UomService } from './uom.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { Uom } from '../../../../core/model/unit-of-measuremrnt.model';
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
  selector: 'app-unit-of-measurements',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, RatingModule, InputTextModule, TextareaModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './unit-of-measurements.html',
  styleUrls: ['./unit-of-measurements.scss'],
  providers: [MessageService, ConfirmationService, UomService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class UnitOfMeasurements implements OnInit {

  submitted: boolean = false;
  statusOptions!: any[];
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];

  uomForm!: FormGroup;

  uomService = inject(UomService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'units_of_measurment')?.id || 0;

  ngOnInit(): void {
    this.uomService.loadUoms();
  }


  exportCSV() {
    this.dt.exportCSV();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.uomService.uomDialog.set(true);
  }

  editUom(uom: Uom) {
    this.uomService.uomDialog.set(true);

    this.setForm(uom);
  }



  hideDialog() {
    this.uomService.uomDialog.set(false);
  }

  deleteUom(uom: Uom) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_uom'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.uomService.deleteUom(uom.id);
      }
    });

  }

  saveUom() {
    if(this.uomForm.invalid){
      this.uomForm.markAllAsTouched();
      return;
    }

    if(this.uomForm.value.id){
      this.uomService.updateUom(this.uomForm.value);
    } else {
      this.uomService.saveUom(this.uomForm.value);
    }
  }

  initiatForm() {
    this.uomForm = this.fb.group({
      id: [null],
     name: ['', Validators.required],
     shortName: [''],
     status: [true],
    });
  }

  setForm(uom: Uom) {
    this.uomForm = this.fb.group({
      id: [uom.id],
      name: [uom.name, Validators.required],
      shortName: [uom.shortName],
      status: [uom.status],
    });
  }


}
