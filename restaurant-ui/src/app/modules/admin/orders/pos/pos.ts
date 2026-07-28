import { Component, effect, inject, OnInit } from '@angular/core';
import { Toast } from "primeng/toast";
import { Button } from "primeng/button";
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { Checkbox } from "primeng/checkbox";
import { DialogModule } from 'primeng/dialog';
import { MessageService } from 'primeng/api';
import { FormArray, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormInput } from "../../../../shared/components/form-input/form-input";
import { CustomerType } from '../../../../core/enum/customerType.enum';
import { Toolbar } from "primeng/toolbar";
import { PosService } from './pos.service';
import { CashRegisterDialog } from '../cash-registers/cash-register-dialog';
import { CashRegisterService } from '../cash-registers/cash-register.service';
import { OrderService } from '../orders/order.service';
import { Router } from '@angular/router';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';

const CART_STORAGE_KEY = 'pos_cart_items';

@Component({
  selector: 'app-pos',
  imports: [Toast, FormsModule, ReactiveFormsModule, Button, FormInput, Checkbox, DialogModule, Toolbar, CashRegisterDialog,
    ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective
  ],
  templateUrl: './pos.html',
  styleUrls: ['./pos.scss'],
})
export class Pos implements OnInit {

  imagesUrl = env.baseUrl;

  readonly translate = inject(TranslateService);
  readonly messageService = inject(MessageService);
  readonly posService = inject(PosService);
  readonly cashRegisterService = inject(CashRegisterService);
  readonly orderService = inject(OrderService);
  readonly router = inject(Router);
  CustomerType = CustomerType;

  // Auto-open cash register dialog if no open register exists
  private registerCheckEffect = effect(() => {
    const register = this.cashRegisterService.currentOpenRegister();
    const loading = this.cashRegisterService.loading();
    if (!loading && register === null) {
      this.cashRegisterService.cashRegisterDialog.set(true);
    }
  });

  // Dialog state
  showItemDialog = false;
  selectedItemFood: any = null;
  dialogVariantId: number | null = null;
  dialogAddOns: number[] = [];
  dialogQuantity = 1;

  ordersMenuItemId: number = env.menuItems.find(item => item.name === 'orders')?.id || 0;
  kitchenDashboardMenuItemId: number = env.menuItems.find(item => item.name === 'kitchen_dashboard')?.id || 0;

  currencySymbol = JSON.parse(localStorage.getItem('applicationSettings') || '{}').currencySymbol;
  taxPercentage = JSON.parse(localStorage.getItem('applicationSettings') || '{}').taxPercentage;

  ngOnInit() {
    this.restoreCartFromStorage();
  }

  get orderItems(): FormArray {
    return this.posService.posForm.get('orderItems') as FormArray;
  }

  get invoiceTotal(): number {
    let total = 0;
    for (const control of this.orderItems.controls) {
      const price = control.get('price')?.value ?? 0;
      const quantity = control.get('quantity')?.value ?? 0;
      total += price * quantity;
    }
    return total;
  }

  // Dialog methods
  openItemDialog(itemFood: any) {
    this.selectedItemFood = itemFood;
    // Auto-select first variant if variants exist
    if (itemFood.variants && itemFood.variants.length > 0) {
      this.dialogVariantId = itemFood.variants[0].id;
    } else {
      this.dialogVariantId = null;
    }
    this.dialogAddOns = [];
    this.dialogQuantity = 1;
    this.showItemDialog = true;
  }

  selectVariant(variantId: number) {
    this.dialogVariantId = variantId;
  }

  toggleDialogAddOn(addOnId: number, checked: boolean) {
    if (checked) {
      this.dialogAddOns = [...this.dialogAddOns, addOnId];
    } else {
      this.dialogAddOns = this.dialogAddOns.filter(id => id !== addOnId);
    }
  }

  getDialogAddOnsPrice(): number {
    if (!this.selectedItemFood?.addOnsAssociations) return 0;
    return this.selectedItemFood.addOnsAssociations
      .filter((a: any) => this.dialogAddOns.includes(a.itemFoodAddOnsId))
      .reduce((sum: number, a: any) => sum + (a.itemFoodAddOnsPrice ?? 0), 0);
  }

  getDialogSelectedPrice(): number {
    let basePrice = this.selectedItemFood?.price ?? 0;

    if (this.dialogVariantId && this.selectedItemFood?.variants) {
      const variant = this.selectedItemFood.variants.find((v: any) => v.id === this.dialogVariantId);
      if (variant) basePrice = variant.price;
    }
    return (basePrice * this.dialogQuantity) + this.getDialogAddOnsPrice();
  }

  addFromDialog() {
    if (!this.selectedItemFood) return;

    const itemFood = this.selectedItemFood;

    // Require variant selection if variants exist
    if (itemFood.variants && itemFood.variants.length > 0 && !this.dialogVariantId) {
      this.messageService.add({
        severity: 'warn',
        summary: this.translate.instant('label_warning'),
        detail: this.translate.instant('msg_select_variant')
      });
      return;
    }

    let basePrice = 0;
    let variantName = '';

    if (this.dialogVariantId && itemFood.variants) {
      const variant = itemFood.variants.find((v: any) => v.id === this.dialogVariantId);
      if (variant) {
        basePrice = variant.price;
        variantName = variant.name;
      }
    } else {
      basePrice = itemFood.price ?? 0;
    }

    const selectedAddOns = (itemFood.addOnsAssociations || [])
      .filter((a: any) => this.dialogAddOns.includes(a.itemFoodAddOnsId));

    const addOnsPrice = selectedAddOns.reduce((sum: number, a: any) => sum + (a.itemFoodAddOnsPrice ?? 0), 0);

    const addOnNames = selectedAddOns
      .map((a: any) => a.itemFoodAddOnsName)
      .join(', ');

    const totalPrice = basePrice + addOnsPrice;

    this.orderItems.push(
      this.posService.fb.group({
        itemFoodId: [itemFood.id],
        itemFoodName: [itemFood.name],
        price: [totalPrice, [Validators.required, Validators.min(0)]],
        quantity: [this.dialogQuantity, [Validators.required, Validators.min(1)]],
        variantId: [this.dialogVariantId],
        variantName: [variantName],
        addOnIds: [this.dialogAddOns],
        addOnNames: [addOnNames],
        addOnsPrice: [addOnsPrice],
      })
    );

    this.saveCartToStorage();

    // Reset dialog for adding another variant of same item
    if (itemFood.variants && itemFood.variants.length > 0) {
      this.dialogVariantId = itemFood.variants[0].id;
    } else {
      this.dialogVariantId = null;
    }
    this.dialogAddOns = [];
    this.dialogQuantity = 1;
  }

  addAndClose() {
    this.addFromDialog();
    this.showItemDialog = false;
  }

  incrementQuantity(index: number) {
    const control = this.orderItems.at(index).get('quantity');
    if (control) {
      control.setValue(control.value + 1);
      this.saveCartToStorage();
    }
  }

  decrementQuantity(index: number) {
    const control = this.orderItems.at(index).get('quantity');
    if (control && control.value > 1) {
      control.setValue(control.value - 1);
      this.saveCartToStorage();
    }
  }

  removeOrderItem(index: number) {
    this.orderItems.removeAt(index);
    this.saveCartToStorage();
  }

  // LocalStorage persistence
  private saveCartToStorage() {
    const items = this.orderItems.controls.map(control => ({
      itemFoodId: control.get('itemFoodId')?.value,
      itemFoodName: control.get('itemFoodName')?.value,
      price: control.get('price')?.value,
      quantity: control.get('quantity')?.value,
      variantId: control.get('variantId')?.value,
      variantName: control.get('variantName')?.value,
      addOnIds: control.get('addOnIds')?.value,
      addOnNames: control.get('addOnNames')?.value,
      addOnsPrice: control.get('addOnsPrice')?.value,
    }));
    localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(items));
  }

  private restoreCartFromStorage() {
    const stored = localStorage.getItem(CART_STORAGE_KEY);
    if (!stored) return;

    try {
      const items = JSON.parse(stored);
      if (Array.isArray(items)) {
        for (const item of items) {
          this.orderItems.push(
            this.posService.fb.group({
              itemFoodId: [item.itemFoodId],
              itemFoodName: [item.itemFoodName],
              price: [item.price, [Validators.required, Validators.min(0)]],
              quantity: [item.quantity, [Validators.required, Validators.min(1)]],
              variantId: [item.variantId],
              variantName: [item.variantName],
              addOnIds: [item.addOnIds],
              addOnNames: [item.addOnNames],
              addOnsPrice: [item.addOnsPrice ?? 0],
            })
          );
        }
      }
    } catch (e) {
      localStorage.removeItem(CART_STORAGE_KEY);
    }
  }

  clearCart() {
    this.orderItems.clear();
    localStorage.removeItem(CART_STORAGE_KEY);
  }


  placeOrder() {
    this.posService.posForm.controls['tableId'].setValidators([Validators.required]);
    this.posService.posForm.controls['tableId'].updateValueAndValidity();

    if (this.posService.posForm.invalid || this.orderItems.length === 0) {
      this.messageService.add({
        severity: 'warn',
        summary: this.translate.instant('label_warning'),
        detail: this.translate.instant('msg_fill_required_fields')
      });
      return;
    }

    const formValue = this.posService.posForm.value;
    this.orderService.createOrder({
      orderType: 'PLACE_ORDER',
      customerType: formValue.customerType,
      customerId: formValue.customerId ?? undefined,
      thirdPartyCustomerId: formValue.thirdPartyCustomerId ?? undefined,
      tableId: formValue.tableId,
      waiterId: formValue.waiterId ?? undefined,
      cashRegisterId: this.cashRegisterService.currentOpenRegister()?.id,
      orderItems: formValue.orderItems.map((item: any) => ({
        itemFoodId: item.itemFoodId,
        price: item.price,
        quantity: item.quantity,
        variantId: item.variantId ?? undefined,
        variantName: item.variantName ?? undefined,
        addOnIds: item.addOnIds ?? [],
        addOnsPrice: item.addOnsPrice ?? 0,
      }))
    }, (order) => {
      this.clearCart();
      this.posService.initializeForm();
      this.router.navigate(['/admin/orders', order.id]);
    });
  }

  quickOrder() {
    this.posService.posForm.controls['tableId'].removeValidators([Validators.required]);
    this.posService.posForm.controls['tableId'].updateValueAndValidity();

    if (this.posService.posForm.invalid || this.orderItems.length === 0) {
      this.messageService.add({
        severity: 'warn',
        summary: this.translate.instant('label_warning'),
        detail: this.translate.instant('msg_fill_required_fields')
      });
      return;
    }

    const formValue = this.posService.posForm.value;
    this.orderService.createOrder({
      orderType: 'QUICK_ORDER',
      customerType: formValue.customerType,
      customerId: formValue.customerId ?? undefined,
      thirdPartyCustomerId: formValue.thirdPartyCustomerId ?? undefined,
      tableId: formValue.tableId ?? undefined,
      waiterId: formValue.waiterId ?? undefined,
      cashRegisterId: this.cashRegisterService.currentOpenRegister()?.id,
      orderItems: formValue.orderItems.map((item: any) => ({
        itemFoodId: item.itemFoodId,
        price: item.price,
        quantity: item.quantity,
        variantId: item.variantId ?? undefined,
        variantName: item.variantName ?? undefined,
        addOnIds: item.addOnIds ?? [],
        addOnsPrice: item.addOnsPrice ?? 0,
      }))
    }, (order) => {
      this.clearCart();
      this.posService.initializeForm();
      this.router.navigate(['/admin/orders', order.id]);
    });
  }

  openOnGoingOrders(){
    this.router.navigate(['/admin/orders']);
  }

  openKitchenStatus(){
    this.router.navigate(['/admin/kitchen-dashboard']);
  }

  openTodayOrders(){
    this.router.navigate(['/admin/orders'], { queryParams: { status: 'NEW' } });
  }

  closeCashRegister() {
    this.cashRegisterService.closeDialog.set(true);
  }
}
