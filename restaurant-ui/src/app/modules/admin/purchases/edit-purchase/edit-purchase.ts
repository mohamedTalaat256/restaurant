import { CommonModule, DecimalPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, effect, inject, OnInit } from '@angular/core';
import { FormArray, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { ProgressBarModule } from 'primeng/progressbar';
import { PurchaseService } from '../purchases/purchase.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { TranslateService } from '../../../../core/service/translate.service';
import { Toast } from 'primeng/toast';
import { SupplierService } from '../suppliers/supplier.service';
import { IngredientService } from '../ingredients/ingredient.service';
import { DatePickerModule } from 'primeng/datepicker';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { PaymentMethod } from '../../../../core/enum/paymentMethod.enum';

@Component({
  selector: 'app-edit-purchase',
  imports: [CommonModule, ButtonModule,
    InputTextModule, TextareaModule, SelectModule,
    ProgressBarModule, DatePickerModule,
    ReactiveFormsModule, FormInput, Toast],
  templateUrl: './edit-purchase.html',
  styleUrls: ['./edit-purchase.scss'],
  providers: [MessageService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditPurchase implements OnInit {



  purchaseService = inject(PurchaseService);
  supplierService = inject(SupplierService);
  ingredientService = inject(IngredientService);
  readonly translate = inject(TranslateService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  isEditMode = false;

  constructor() {
    effect(() => {
      if (this.purchaseService.savedSuccess()) {
        this.purchaseService.savedSuccess.set(false);
        this.router.navigate(['/admin/purchases']);
      }
    });
  }

  supplierOptions = computed(() => {
    return this.supplierService.suppliers().map(s => ({ label: s.name, value: s.id }));
  });

  ingredientOptions = computed(() => {
    return this.ingredientService.ingredients().map(i => ({ label: i.name, value: i.id }));
  });

  paymentMethodOptions = computed(() => {
    return Object.values(PaymentMethod).map(m => ({ label: this.translate.instant(`label_${m.toLowerCase()}`), value: m }));
  });

  totalAmount = this.purchaseService.totalAmount;

  ngOnInit(): void {
    this.supplierService.loadSuppliers();
    this.ingredientService.loadIngredients();

    const id = this.route.snapshot.paramMap.get('id');
    if (id && id !== 'new') {
      this.isEditMode = true;
      this.purchaseService.loadPurchaseById(+id);
    }else{
      this.purchaseService.initiatForm();
    }

  }

  get purchaseItems(): FormArray {
    return this.purchaseService.purchaseForm.get('purchaseItems') as FormArray;
  }

  addPurchaseItem() {
    this.purchaseItems.push(this.purchaseService.fb.group({
      ingredientId: [null, Validators.required],
      quantity: [1, Validators.required],
      price: [null, Validators.required],
    }));
  }

  removePurchaseItem(index: number) {
    this.purchaseItems.removeAt(index);
  }


  savePurchase() {
    if (this.purchaseService.purchaseForm.invalid) {
      this.purchaseService.purchaseForm.markAllAsTouched();
      return;
    }

    const formValue = { ...this.purchaseService.purchaseForm.value };
    if (formValue.id) {
      this.purchaseService.updatePurchase(formValue);
    } else {
      this.purchaseService.savePurchase(formValue);
    }
  }

  goBack() {
    this.router.navigate(['/admin/purchases']);
  }

}
