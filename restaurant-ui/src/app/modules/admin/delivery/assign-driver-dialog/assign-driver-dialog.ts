import { Component, inject, Input, OnChanges, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { DeliveryService } from '../delivery.service';
import { DriverService } from '../driver.service';
import { TranslateService } from '../../../../core/service/translate.service';
import { Driver } from '../../../../core/model/driver.model';

@Component({
  selector: 'app-assign-driver-dialog',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    DialogModule,
    ButtonModule,
    SelectModule,
    TextareaModule,
    Toast,
  ],
  templateUrl: './assign-driver-dialog.html',
  providers: [MessageService],
})
export class AssignDriverDialogComponent implements OnChanges {
  @Input() visible = false;
  @Input() deliveryId = 0;
  @Output() visibleChange = new EventEmitter<boolean>();
  @Output() assigned = new EventEmitter<void>();

  readonly deliveryService = inject(DeliveryService);
  readonly driverService = inject(DriverService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  form = this.fb.group({
    driverId: [null as number | null, Validators.required],
    reason: [''],
  });

  ngOnChanges() {
    if (this.visible) {
      this.form.reset();
      this.driverService.loadAvailableDrivers();
    }
  }

  get driverOptions() {
    return this.driverService.availableDrivers().map((d: Driver) => ({
      label: `${d.fullName} — ${d.vehicleType} (${d.vehiclePlate})`,
      value: d.id,
    }));
  }

  get noDrivers(): boolean {
    return !this.driverService.loading() && this.driverService.availableDrivers().length === 0;
  }

  close() {
    this.visibleChange.emit(false);
  }

  submit() {
    if (this.form.invalid) return;
    const val = this.form.value;
    this.deliveryService.assignDriver(
      this.deliveryId,
      { driverId: val.driverId!, reason: val.reason || null },
      () => {
        this.assigned.emit();
        this.visibleChange.emit(false);
      },
    );
  }
}
