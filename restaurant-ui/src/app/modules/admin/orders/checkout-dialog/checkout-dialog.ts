import { Component, EventEmitter, inject, Input, OnChanges, Output } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputNumberModule } from 'primeng/inputnumber';
import { CheckboxModule } from 'primeng/checkbox';
import { OrderService } from '../orders/order.service';
import { TranslateService } from '../../../../core/service/translate.service';

@Component({
  selector: 'app-checkout-dialog',
  imports: [DialogModule, ButtonModule, InputNumberModule, FormsModule, DecimalPipe, CheckboxModule],
  template: `
    <p-dialog
      [header]="translate.instant('label_checkout')"
      [visible]="visible"
      (visibleChange)="visibleChange.emit($event)"
      [modal]="true"
      [style]="{ width: '420px' }"
      [closable]="true"
    >
      <ng-template #content>
        <div class="d-flex flex-column gap-3 py-3">
          <div class="d-flex justify-content-between">
            <span>{{ translate.instant('label_total') }}</span>
            <strong>{{ totalAmount | number: '1.2-2' }}</strong>
          </div>

          <div>
            <label class="f-w-bold mb-1 d-block">{{ translate.instant('label_paid_amount') }}</label>
            <p-inputnumber
              [(ngModel)]="paidAmount"
              [min]="0"
              [minFractionDigits]="2"
              [maxFractionDigits]="2"
              styleClass="w-100"
              (ngModelChange)="onPaidAmountChange()"
            />
          </div>

          <div class="d-flex align-items-center gap-2">
            <p-checkbox [(ngModel)]="isCash" [binary]="true" inputId="isCash" />
            <label for="isCash">{{ translate.instant('label_cash_payment') }}</label>
          </div>

          @if (isCash && paidAmount > 0) {
            <div class="d-flex justify-content-between">
              <span>{{ translate.instant('label_change') }}</span>
              <strong [class]="change >= 0 ? 'text-success' : 'text-danger'">{{ change | number: '1.2-2' }}</strong>
            </div>
            <div class="d-flex justify-content-between">
              <span>{{ translate.instant('label_remaining') }}</span>
              <strong>{{ remaining | number: '1.2-2' }}</strong>
            </div>
          }
        </div>
      </ng-template>
      <ng-template #footer>
        <p-button [label]="translate.instant('label_cancel')" icon="pi pi-times" [text]="true" (click)="visibleChange.emit(false)" />
        <p-button
          [label]="translate.instant('label_checkout')"
          icon="pi pi-credit-card"
          severity="success"
          [loading]="orderService.loadingSave()"
          [disabled]="paidAmount <= 0"
          (click)="submit()"
        />
      </ng-template>
    </p-dialog>
  `,
})
export class CheckoutDialogComponent implements OnChanges {
  @Input() visible = false;
  @Input() orderId!: number;
  @Input() totalAmount = 0;
  @Output() visibleChange = new EventEmitter<boolean>();
  @Output() checkoutDone = new EventEmitter<void>();

  readonly orderService = inject(OrderService);
  readonly translate = inject(TranslateService);

  paidAmount = 0;
  isCash = true;
  change = 0;
  remaining = 0;

  ngOnChanges() {
    if (this.visible) {
      this.paidAmount = this.totalAmount;
      this.onPaidAmountChange();
    }
  }

  onPaidAmountChange() {
    this.change = this.paidAmount - this.totalAmount;
    this.remaining = Math.max(0, this.totalAmount - this.paidAmount);
  }

  submit() {
    if (this.paidAmount <= 0 || !this.orderId) return;
    this.orderService.checkoutOrder(
      this.orderId,
      { paidAmount: this.paidAmount, isCash: this.isCash },
      () => {
        this.visibleChange.emit(false);
        this.checkoutDone.emit();
      }
    );
  }
}
