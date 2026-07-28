/* import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { ProgressBarModule } from 'primeng/progressbar';
import { MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { TranslateService } from '../../../../core/service/translate.service';
import { JournalEntryService } from './journal-entry.service';
import { CostCenterService } from '../cost-centers/cost-center.service';
import { AccountsService } from '../accounts.service';
import { SelectModule } from 'primeng/select';
import { TagModule } from 'primeng/tag';

@Component({
  selector: 'app-journal-entries',
  imports: [CommonModule, ButtonModule,
    ToolbarModule, InputTextModule, TableModule,
    ProgressBarModule, SelectModule, TagModule,
    ReactiveFormsModule, FormInput, Toast, CardModule],
  templateUrl: './journal-entries.html',
  styleUrls: ['./journal-entries.scss'],
  providers: [MessageService, JournalEntryService, CostCenterService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class JournalEntries implements OnInit {

  journalEntryService = inject(JournalEntryService);
  costCenterService = inject(CostCenterService);
  accountsService = inject(AccountsService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  entryForm!: FormGroup;

  ngOnInit(): void {
    this.initForm();
    this.accountsService.loadAccountsFlat();
    this.costCenterService.loadActiveCostCenters();
  }

  get items(): FormArray {
    return this.entryForm.get('items') as FormArray;
  }

  get accountOptions() {
    return this.accountsService.accountsFlat()
      .filter(a => a.allowTransaction)
      .map(a => ({ label: ' [ ' +a.balance+' ] '+a.code + ' - ' + a.name, value: a.id }));
  }

  get costCenterOptions() {
    return this.costCenterService.activeCostCenters()
      .map(c => ({ label: c.code + ' - ' + c.name, value: c.id }));
  }

  get totalDebit(): number {
    return this.items.controls.reduce((sum, c) => sum + (+(c.get('debit')?.value) || 0), 0);
  }

  get totalCredit(): number {
    return this.items.controls.reduce((sum, c) => sum + (+(c.get('credit')?.value) || 0), 0);
  }

  get isBalanced(): boolean {
    return Math.abs(this.totalDebit - this.totalCredit) < 0.001;
  }

  initForm() {
    this.entryForm = this.fb.group({
      entryDate: [null, [Validators.required]],
      description: ['', [Validators.required]],
      reference: [''],
      items: this.fb.array([this.createItem(), this.createItem()])
    });
  }

  createItem(): FormGroup {
    return this.fb.group({
      accountId: [null, [Validators.required]],
      debit: [0, [Validators.required, Validators.min(0)]],
      credit: [0, [Validators.required, Validators.min(0)]],
      costCenterId: [null],
    });
  }

  addItem() {
    this.items.push(this.createItem());
  }

  removeItem(index: number) {
    if (this.items.length > 2) {
      this.items.removeAt(index);
    }
  }

  submitEntry() {
    if (this.entryForm.invalid) {
      this.entryForm.markAllAsTouched();
      return;
    }

    if (!this.isBalanced) {
      return;
    }

    const formValue = this.entryForm.value;
    const request = {
      entryDate: formValue.entryDate instanceof Date ? formValue.entryDate.toISOString() : formValue.entryDate,
      description: formValue.description,
      reference: formValue.reference,
      items: formValue.items
    };

    this.journalEntryService.createManualEntry(request);
  }

  resetForm() {
    this.entryForm.reset();
    this.items.clear();
    this.items.push(this.createItem());
    this.items.push(this.createItem());
  }
}
 */


import { Component, computed, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { DatePickerModule } from 'primeng/datepicker'; // في PrimeNG v21 الـ Calendar بقى اسمه DatePicker
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { SelectModule } from 'primeng/select';
import { Card } from "primeng/card";
import { TranslateService } from '../../../../core/service/translate.service';
import { FormInput } from "../../../../shared/components/form-input/form-input";
import { AccountsService } from '../accounts.service';
import { CostCenterService } from '../cost-centers/cost-center.service';
import { JournalEntryService } from './journal-entry.service';

// تعريف الـ Enum ليتطابق مع الباك إند
export enum FinancialTransactionType {
  SUPPLIER_PAYMENT = 'SUPPLIER_PAYMENT',
  CUSTOMER_COLLECTION = 'CUSTOMER_COLLECTION',
  OPERATIONAL_EXPENSE = 'OPERATIONAL_EXPENSE',
  EMPLOYEE_ADVANCE = 'EMPLOYEE_ADVANCE',
  INTERNAL_TRANSFER = 'INTERNAL_TRANSFER'
}

@Component({
  selector: 'app-financial-transaction',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputNumberModule,
    InputTextModule,
    FormsModule,
    ReactiveFormsModule,
    DatePickerModule,
    ButtonModule,
    ToastModule,
    SelectModule,
    Card,
    FormInput
  ],
  providers: [MessageService],
  templateUrl: './journal-entries.html',
  styleUrls: ['./journal-entries.scss'],
})
export class JournalEntries implements OnInit {
  transactionForm!: FormGroup;
  loading: boolean = false;
  readonly translate = inject(TranslateService);
  readonly accountsService = inject(AccountsService);
  readonly costCenterService = inject(CostCenterService);
  readonly journalEntryService = inject(JournalEntryService);

  transactionTypes = computed(() => Object.values(FinancialTransactionType).map(type => ({ label: this.translate.instant('enum_' + type), value: type })));

  sourceAccountOptions = computed(() => this.accountsService.accountsFlat().map(a => a.allowTransaction ? { label: a.code + ' - ' + a.name, value: a.id } : null).filter(a => a !== null));
  destinationAccountOptions = computed(() => this.accountsService.accountsFlat().map(a => a.allowTransaction ? { label: a.code + ' - ' + a.name, value: a.id } : null).filter(a => a !== null));
  costCenterOptions = computed(() => this.costCenterService.activeCostCenters().map(c => ({ label: c.name, value: c.id })));

  constructor(private fb: FormBuilder, private messageService: MessageService) { }

  ngOnInit(): void {
    this.initForm();
    this.accountsService.loadAccountsFlat();
    this.accountsService.loadTransactionTypes();
    this.costCenterService.loadActiveCostCenters();
  }

  initForm() {
    this.transactionForm = this.fb.group({
      transactionDate: [new Date(), Validators.required],
      transactionType: [null, Validators.required],
      sourceAccountId: [null, Validators.required],
      destinationAccountId: [null, Validators.required],
      costCenterId: [null, [Validators.required]],
      amount: [null, [Validators.required, Validators.min(0.01)]],
      description: ['', Validators.required],
      reference: ['']
    });
  }


  onSubmit() {
    if (this.transactionForm.invalid) {
      return;
    }

    this.loading = true;
    const payload = this.transactionForm.value;

    // هنا بتبعت الـ payload للـ HttpClient للباك إند
    console.log('Sending Payload to Backend:', payload);

    this.journalEntryService.post(payload);
  }
}
