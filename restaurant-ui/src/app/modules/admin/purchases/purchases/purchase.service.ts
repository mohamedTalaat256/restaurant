import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { env } from '../../../../../environment/env';
import { TranslateService } from '../../../../core/service/translate.service';
import { Purchase } from '../../../../core/model/purchase.model';
import { FormMode } from '../../../../core/enum/formModeEnum';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { PaymentMethod } from '../../../../core/enum/paymentMethod.enum';

@Injectable({ providedIn: 'root' })
export class PurchaseService {

  totalAmount = signal<number>(0);
  purchases = signal<Purchase[]>([]);
  purchase = signal<Purchase | null>(null);
  loading = signal(false);
  loadingSave = signal(false);
  purchaseDialog = signal(false);
  savedSuccess = signal(false);

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  fb = inject(FormBuilder);
  purchaseForm!: FormGroup;
  readonly translate = inject(TranslateService);
  private itemsSubscription?: Subscription;
  private paymentMethodSubscription?: Subscription;



  loadPurchases() {
    this.loading.set(true);
    this.error.set(null);
    this.http.get<ApiResponse<Purchase[]>>(env.apiUrl + '/purchase/purchases').subscribe({
      next: (res) => {
        this.purchases.set(res.data);
        this.loading.set(false);
      },
      error: () => { this.loading.set(false); }
    });
  }

  loadPurchaseById(id: number) {
    this.loading.set(true);
    this.error.set(null);
    this.http.get<ApiResponse<Purchase>>(env.apiUrl + '/purchase/purchases/' + id).subscribe({
      next: (res) => {
        this.purchase.set(res.data);
        this.setForm(res.data);
        this.loading.set(false);
      },
      error: () => { this.loading.set(false); }
    });
  }

  savePurchase(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);
    let formMode = FormMode.CREATE;
    if (formValue.id) formMode = FormMode.EDIT;
    console.log('formValue', formValue);

    this.http.post<any>(env.apiUrl + '/purchase/purchases', formValue).subscribe({
      next: (res: ApiResponse<Purchase>) => {
        if (res.status) {
          if (formMode === FormMode.CREATE) {
            this.purchases.update((items) => [...items, res.data]);
          } else {
            this.purchases.update((items) => items.map(item => item.id === res.data.id ? res.data : item));
          }
          this.loadingSave.set(false);
          this.purchaseDialog.set(false);
          this.savedSuccess.set(true);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  updatePurchase(formValue: any) {
    this.loadingSave.set(true);
    this.error.set(null);
    this.http.put<any>(env.apiUrl + '/purchase/purchases/' + formValue.id, formValue).subscribe({
      next: (res: ApiResponse<Purchase>) => {
        if (res.status) {
          this.purchases.update((items) => items.map(item => item.id === res.data.id ? res.data : item));
          this.loadingSave.set(false);
          this.purchaseDialog.set(false);
          this.savedSuccess.set(true);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  deletePurchase(id: number) {
    this.http.delete<any>(env.apiUrl + '/purchase/purchases/' + id).subscribe({
      next: (res: ApiResponse<any>) => {
        if (res.status) {
          this.purchases.update((items) => items.filter(item => item.id !== id));
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        }
      },
      error: () => {}
    });
  }

  approvePurchase(id: number) {
    this.loadingSave.set(true);
    this.http.patch<ApiResponse<Purchase>>(env.apiUrl + '/purchase/purchases/' + id + '/approve', {}).subscribe({
      next: (res) => {
        if (res.status) {
          this.purchase.set(res.data);
          this.purchases.update((items) => items.map(item => item.id === res.data.id ? res.data : item));
          this.savedSuccess.set(true);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
        this.loadingSave.set(false);
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  voidPurchase(id: number) {
    this.loadingSave.set(true);
    this.http.patch<ApiResponse<Purchase>>(env.apiUrl + '/purchase/purchases/' + id + '/void', {}).subscribe({
      next: (res) => {
        if (res.status) {
          this.purchases.update((items) => items.map(item => item.id === res.data.id ? res.data : item));
          this.messageService.add({ severity: 'success', summary: this.translate.instant('label_successful'), detail: this.translate.instant(res.message), life: 3000 });
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('label_failed'), detail: this.translate.instant(res.message), life: 3000 });
        }
        this.loadingSave.set(false);
      },
      error: () => { this.loadingSave.set(false); }
    });
  }

  recalCulateTotalAmount(){
    const items = this.purchaseForm.get('purchaseItems') as FormArray;
    const total = items.value.reduce((sum:number, item:any) => sum + ((item.quantity || 0) * (item.price || 0)), 0);
    this.totalAmount.set(total);
    this.syncPaidAmountIfCash();
  }

  private syncPaidAmountIfCash() {
    const method = this.purchaseForm.get('paymentMethod')?.value;
    if (method === PaymentMethod.CASH) {
      this.purchaseForm.get('paidAmount')?.setValue(this.totalAmount(), { emitEvent: false });
    }
  }

  private subscribeToPurchaseItems() {
    this.itemsSubscription?.unsubscribe();
    this.paymentMethodSubscription?.unsubscribe();
    const items = this.purchaseForm.get('purchaseItems') as FormArray;
    this.itemsSubscription = items.valueChanges.subscribe(() => {
      this.recalCulateTotalAmount();
    });
    this.paymentMethodSubscription = this.purchaseForm.get('paymentMethod')?.valueChanges.subscribe(() => {
      this.syncPaidAmountIfCash();
    });
    this.recalCulateTotalAmount();
  }

  initiatForm() {
    this.purchaseForm = this.fb.group({
      id: [null],
      invoiceNumber: ['', Validators.required],
      paymentMethod: [null, Validators.required],
      supplierId: [null, Validators.required],
      purchaseDate: [null, Validators.required],
      paidAmount: [0, Validators.required],
      note: [''],
      purchaseItems: this.fb.array([]),
    });
    this.subscribeToPurchaseItems();
  }

  setForm(purchase: Purchase) {
    this.purchaseForm = this.fb.group({
      id: [purchase.id],
      invoiceNumber: [purchase.invoiceNumber, Validators.required],
      paymentMethod: [purchase.paymentMethod, Validators.required],
      supplierId: [purchase.supplierId, Validators.required],
      purchaseDate: [purchase.purchaseDate ? new Date(purchase.purchaseDate) : null, Validators.required],
      paidAmount: [purchase.paidAmount, Validators.required],
      note: [purchase.note],
      purchaseItems: this.fb.array(
        (purchase.purchaseItems || []).map(item => this.fb.group({
          id: [item.id],
          ingredientId: [item.ingredientId || item.ingredient?.id, Validators.required],
          quantity: [item.quantity, Validators.required],
          price: [item.price, Validators.required],
          productionDate: [item.productionDate ? new Date(item.productionDate) : null],
          expiryDate: [item.expiryDate ? new Date(item.expiryDate) : null],
        }))
      ),
    });
    this.subscribeToPurchaseItems();
  }
}
