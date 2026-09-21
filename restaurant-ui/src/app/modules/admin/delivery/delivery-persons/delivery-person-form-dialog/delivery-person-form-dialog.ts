import { ChangeDetectionStrategy, Component, effect, EventEmitter, inject, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { MessageModule } from 'primeng/message';
import { DeliveryPersonService } from '../delivery-person.service';
import { FormInput } from '../../../../../shared/components/form-input/form-input';
import { DeliveryDetails, DELIVERY_VEHICLE_TYPE_OPTIONS } from '../../../../../core/model/delivery-details.model';
import { TranslateService } from '../../../../../core/service/translate.service';

@Component({
  selector: 'app-delivery-person-form-dialog',
  imports: [DialogModule, ReactiveFormsModule, FormInput, ButtonModule, MessageModule],
  templateUrl: './delivery-person-form-dialog.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DeliveryPersonFormDialog implements OnChanges {
  @Input() visible = false;
  @Input() delivery: DeliveryDetails | null = null;
  @Output() visibleChange = new EventEmitter<boolean>();
  @Output() saved = new EventEmitter<void>();

  deliveryForm!: FormGroup;
  isEditMode = false;

  readonly vehicleTypeOptions = DELIVERY_VEHICLE_TYPE_OPTIONS;

  deliveryService = inject(DeliveryPersonService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  constructor() {
    effect(() => {
      if (this.visible && this.deliveryService.savedSuccess()) {
        this.deliveryService.savedSuccess.set(false);
        this.saved.emit();
        this.visibleChange.emit(false);
      }
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if ((changes['visible'] || changes['delivery']) && this.visible) {
      this.initForm(this.delivery);
    }
  }

  hideDialog(): void {
    this.visibleChange.emit(false);
  }

  saveDelivery(): void {
    if (this.deliveryForm.invalid) {
      this.deliveryForm.markAllAsTouched();
      return;
    }

    const payload = { ...this.deliveryForm.value };
    // On edit, drop empty password so the backend keeps the current one.
    if (this.isEditMode && !payload.password) {
      delete payload.password;
    }

    if (this.isEditMode) {
      this.deliveryService.updateDelivery(payload);
    } else {
      this.deliveryService.saveDelivery(payload);
    }
  }

  private initForm(delivery: DeliveryDetails | null): void {
    this.isEditMode = !!delivery?.id;

    const passwordValidators = this.isEditMode
      ? [Validators.minLength(6)]
      : [Validators.required, Validators.minLength(6)];

    this.deliveryForm = this.fb.group({
      id: [delivery?.id ?? null],
      firstname: [delivery?.firstname ?? '', Validators.required],
      lastname: [delivery?.lastname ?? '', Validators.required],
      email: [delivery?.email ?? '', [Validators.required, Validators.email]],
      password: ['', passwordValidators],
      phone: [delivery?.phone ?? '', Validators.required],
      vehicleType: [delivery?.vehicleType ?? null],
      vehicleNumber: [delivery?.vehicleNumber ?? ''],
      status: [delivery?.status ?? true],
    });
  }
}
