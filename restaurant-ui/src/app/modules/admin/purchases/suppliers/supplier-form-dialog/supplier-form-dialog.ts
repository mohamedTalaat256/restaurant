import { ChangeDetectionStrategy, Component, effect, EventEmitter, inject, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { SupplierService } from '../supplier.service';
import { FormInput } from '../../../../../shared/components/form-input/form-input';
import { Supplier } from '../../../../../core/model/supplier.model';
import { TranslateService } from '../../../../../core/service/translate.service';

@Component({
  selector: 'app-supplier-form-dialog',
  imports: [DialogModule, ReactiveFormsModule, FormInput, ButtonModule],
  templateUrl: './supplier-form-dialog.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SupplierFormDialog implements OnChanges {
  @Input() visible = false;
  @Input() supplier: Supplier | null = null;
  @Output() visibleChange = new EventEmitter<boolean>();
  @Output() saved = new EventEmitter<void>();

  supplierForm!: FormGroup;

  supplierService = inject(SupplierService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  constructor() {
    effect(() => {
      if (this.visible && this.supplierService.savedSuccess()) {
        this.supplierService.savedSuccess.set(false);
        this.saved.emit();
        this.visibleChange.emit(false);
      }
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if ((changes['visible'] || changes['supplier']) && this.visible) {
      this.initForm(this.supplier);
    }
  }

  hideDialog(): void {
    this.visibleChange.emit(false);
  }

  saveSupplier(): void {
    if (this.supplierForm.invalid) {
      this.supplierForm.markAllAsTouched();
      return;
    }

    const isEditMode = !!this.supplierForm.value.id;
    if (isEditMode) {
      this.supplierService.updateSupplier(this.supplierForm.value);
    } else {
      this.supplierService.saveSupplier(this.supplierForm.value);
    }
  }

  private initForm(supplier: Supplier | null): void {
    this.supplierForm = this.fb.group({
      id: [supplier?.id ?? null],
      name: [supplier?.name ?? '', Validators.required],
      email: [supplier?.email ?? '', Validators.email],
      phone: [supplier?.phone ?? ''],
      address: [supplier?.address ?? ''],
      status: [supplier?.status ?? true],
    });
  }
}
