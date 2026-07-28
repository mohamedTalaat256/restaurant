import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { ProgressBarModule } from 'primeng/progressbar';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { TranslateService } from '../../../../core/service/translate.service';
import { CostCenterService } from './cost-center.service';
import { CostCenter } from '../../../../core/model/cost-center.model';

@Component({
  selector: 'app-cost-centers',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast],
  templateUrl: './cost-centers.html',
  styleUrls: ['./cost-centers.scss'],
  providers: [MessageService, ConfirmationService, CostCenterService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CostCenters implements OnInit {

  @ViewChild('dt') dt!: Table;

  costCenterForm!: FormGroup;

  costCenterService = inject(CostCenterService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  ngOnInit(): void {
    this.costCenterService.loadCostCenters();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initForm();
    this.costCenterService.costCenterDialog.set(true);
  }

  editCostCenter(costCenter: CostCenter) {
    this.costCenterService.costCenterDialog.set(true);
    this.setForm(costCenter);
  }

  hideDialog() {
    this.costCenterService.costCenterDialog.set(false);
  }

  deleteCostCenter(costCenter: CostCenter) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_cost_center'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.costCenterService.deleteCostCenter(costCenter.id);
      }
    });
  }

  saveCostCenter() {
    if (this.costCenterForm.invalid) {
      this.costCenterForm.markAllAsTouched();
      return;
    }

    if (this.costCenterForm.value.id) {
      this.costCenterService.updateCostCenter(this.costCenterForm.value);
    } else {
      this.costCenterService.saveCostCenter(this.costCenterForm.value);
    }
  }

  initForm() {
    this.costCenterForm = this.fb.group({
      id: [null],
      code: ['', [Validators.required]],
      name: ['', [Validators.required]],
      status: [true],
    });
  }

  setForm(costCenter: CostCenter) {
    this.costCenterForm = this.fb.group({
      id: [costCenter.id],
      code: [costCenter.code, [Validators.required]],
      name: [costCenter.name, [Validators.required]],
      status: [costCenter.status],
    });
  }
}
