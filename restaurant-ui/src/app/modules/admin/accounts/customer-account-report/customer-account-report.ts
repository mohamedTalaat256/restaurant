import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, OnInit, ViewChild } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { ProgressBarModule } from 'primeng/progressbar';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Toast } from "primeng/toast";
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';
import { AccountsService } from '../accounts.service';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from '../../settings/customers/customer.service';

@Component({
  selector: 'app-customer-account-report',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, Toast],
  templateUrl: './customer-account-report.html',
  styleUrls: ['./customer-account-report.scss'],
  providers: [ConfirmationService, AccountsService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class CustomerAccountReport implements OnInit {

  @ViewChild('dt') dt!: Table;

  accountService = inject(AccountsService);
  customerService = inject(CustomerService);
  readonly translate = inject(TranslateService);

  currencyName =  JSON.parse(localStorage.getItem('applicationSettings')!).currencyName;

  menuItemId: number = env.menuItems.find(item => item.name === 'customer_account_report')?.id || 0;

  customerOptions = computed(() => {
    return this.customerService.customers().map(customer => ({ label: customer.name, value: customer.id }));
  });

  ngOnInit(): void {
    this.customerService.loadCustomers();
  }

  onSelectCustomer(event:any) {
    console.log(event.value.value);
    this.accountService.loadCustomerJournals(event.value.value);
  }


}
