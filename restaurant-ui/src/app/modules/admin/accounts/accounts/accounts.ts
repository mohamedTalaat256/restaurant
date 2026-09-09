import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
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
 import { FormInput } from '../../../../shared/components/form-input/form-input';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { AccountsService } from '../accounts.service';
import { Tab, TabList, TabPanel, TabPanels, Tabs, TabsModule } from 'primeng/tabs';
import { Tree } from 'primeng/tree';
import { Tag } from 'primeng/tag';
import { ApplicationSettingService } from '../../settings/application-settings/application-setting.service';
@Component({
  selector: 'app-acccounts',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, Toast,
  Tabs, TabList, Tab, TabPanels, TabPanel, // نظام التابات الجديد
    Tree, Tag,FormsModule
  ],
  templateUrl: './accounts.html',
  styleUrls: ['./accounts.scss'],
  providers: [ConfirmationService, AccountsService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class Accounts implements OnInit {

  submitted: boolean = false;

  @ViewChild('dt') dt!: Table;
  accountsService = inject(AccountsService);
  readonly settingsService = inject(ApplicationSettingService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'accounts')?.id || 0;

  currencySymbol = this.settingsService.setting()?.currencySymbol || 'EGP';

  searchTerm: string = '';

  ngOnInit(): void {
    this.accountsService.loadAccountsTree();
    this.accountsService.loadAccountsFlat();
    this.accountsService.loadAccountTypes();
    this.settingsService.loadSettingFromLocalStorage();
  }


  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }




}
