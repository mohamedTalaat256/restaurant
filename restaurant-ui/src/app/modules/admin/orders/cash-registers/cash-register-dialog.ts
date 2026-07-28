import { Component, computed, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { CashRegisterService } from './cash-register.service';
import { CashCounterService } from '../cash-counters/cash-counter.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { TranslateService } from '../../../../core/service/translate.service';

@Component({
  selector: 'app-cash-register-dialog',
  imports: [DialogModule, ButtonModule, ReactiveFormsModule, FormInput],
  template: `
    <!-- Open Cash Register Dialog -->
    <p-dialog [visible]="cashRegisterService.cashRegisterDialog()" [style]="{ width: '450px' }"
      [header]="translate.instant('label_open_cash_register')" [modal]="true" [closable]="false">
      <ng-template #content>
        <form [formGroup]="openForm" class="pb-3">
          <div class="mt-4 pt-3">
            <app-form-input [control]="$any(openForm.get('cashCounterId'))" label="label_cash_counter" [options]="cashCounterOptions()" type="select" > </app-form-input>
            <app-form-input [control]="$any(openForm.get('openingBalance'))" label="label_opening_balance" type="number"> </app-form-input>
            <app-form-input [control]="$any(openForm.get('openingNote'))" label="label_opening_note"> </app-form-input>
          </div>
        </form>
      </ng-template>
      <ng-template #footer>
        <p-button [label]="translate.instant('label_open')" icon="pi pi-lock-open" severity="success"
          (click)="openCashRegister()" [loading]="cashRegisterService.loadingSave()" />
      </ng-template>
    </p-dialog>

    <!-- Close Cash Register Dialog -->
    <p-dialog [visible]="cashRegisterService.closeDialog()" [style]="{ width: '450px' }"
      [header]="translate.instant('label_close_cash_register')" [modal]="true" (onHide)="hideCloseDialog()">
      <ng-template #content>
        <form [formGroup]="closeForm" class="pb-3">
          <div class="mt-4 pt-3">
            <app-form-input [control]="$any(closeForm.get('closingBalance'))" label="label_closing_balance" type="number"> </app-form-input>
            <app-form-input [control]="$any(closeForm.get('closingNote'))" label="label_closing_note"> </app-form-input>
          </div>
        </form>
      </ng-template>
      <ng-template #footer>
        <p-button [label]="translate.instant('label_cancel')" icon="pi pi-times" text (click)="hideCloseDialog()" />
        <p-button [label]="translate.instant('label_close')" icon="pi pi-lock" severity="danger"
          (click)="closeCashRegister()" [loading]="cashRegisterService.loadingSave()" />
      </ng-template>
    </p-dialog>
  `,
})
export class CashRegisterDialog implements OnInit {

  readonly cashRegisterService = inject(CashRegisterService);
  readonly cashCounterService = inject(CashCounterService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  openForm!: FormGroup;
  closeForm!: FormGroup;

  cashCounterOptions = computed(() =>
    this.cashCounterService.cashCounters().map(c => ({ label: c.number.toString(), value: c.id }))
  );

  ngOnInit() {
    this.cashCounterService.loadCashCounters();
    this.initOpenForm();
    this.initCloseForm();
  }

  openCashRegister() {
    if (this.openForm.invalid) {
      this.openForm.markAllAsTouched();
      return;
    }
    this.cashRegisterService.openCashRegister(this.openForm.value);
  }

  hideCloseDialog() {
    this.cashRegisterService.closeDialog.set(false);
  }

  closeCashRegister() {
    if (this.closeForm.invalid) {
      this.closeForm.markAllAsTouched();
      return;
    }
    this.cashRegisterService.closeCashRegister(this.closeForm.value);
  }

  initOpenForm() {
    this.openForm = this.fb.group({
      cashCounterId: [null, Validators.required],
      openingBalance: [0, [Validators.required]],
      openingNote: [''],
    });
  }

  initCloseForm() {
    this.closeForm = this.fb.group({
      closingBalance: [0, [Validators.required]],
      closingNote: [''],
    });
  }
}
