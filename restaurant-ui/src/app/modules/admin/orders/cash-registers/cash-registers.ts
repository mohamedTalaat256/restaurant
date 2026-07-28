import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit, ViewChild } from '@angular/core';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { ProgressBarModule } from 'primeng/progressbar';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Toast } from "primeng/toast";
import { CashRegisterService } from './cash-register.service';
import { CashRegisterDialog } from './cash-register-dialog';
import { TranslateService } from '../../../../core/service/translate.service';

@Component({
  selector: 'app-cash-registers',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule,
    TagModule, InputIconModule,
    IconFieldModule,
    ProgressBarModule,
    Toast, CashRegisterDialog],
  templateUrl: './cash-registers.html',
  styleUrls: ['./cash-registers.scss'],
  providers: [MessageService, ConfirmationService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CashRegisters implements OnInit {

  @ViewChild('dt') dt!: Table;

  cashRegisterService = inject(CashRegisterService);
  readonly translate = inject(TranslateService);

  ngOnInit(): void {
    this.cashRegisterService.loadCashRegisters();
    this.cashRegisterService.getMyOpenCashRegister();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  showOpenDialog() {
    this.cashRegisterService.cashRegisterDialog.set(true);
  }

  showCloseDialog() {
    this.cashRegisterService.closeDialog.set(true);
  }
}
