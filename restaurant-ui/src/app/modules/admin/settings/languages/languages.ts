import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
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
import { LanguageService } from './language.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { Language } from '../../../../core/model/language.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';

interface Column {
  field: string;
  header: string;
  customExportHeader?: string;
}

interface ExportColumn {
  title: string;
  dataKey: string;
}


@Component({
  selector: 'app-languages',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './languages.html',
  styleUrls: ['./languages.scss'],
  providers: [MessageService, ConfirmationService, LanguageService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class Languages implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];

  languageForm!: FormGroup;

  languageService = inject(LanguageService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'languages')?.id || 0;

  ngOnInit(): void {
    this.languageService.loadLanguages();
  }

  exportCSV() {
    this.dt.exportCSV();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.languageService.languageDialog.set(true);
  }

  editLanguage(language: Language) {
    this.languageService.languageDialog.set(true);
    this.setForm(language);
  }

  hideDialog() {
    this.languageService.languageDialog.set(false);
  }

  deleteLanguage(language: Language) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_language'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.languageService.deleteLanguage(language.languageCode);
      }
    });
  }

  saveLanguage() {
    if (this.languageForm.invalid) {
      this.languageForm.markAllAsTouched();
      return;
    }
    this.languageService.saveLanguage(this.languageForm.value);

  }

  updateLanguage() {
    if (this.languageForm.invalid) {
      this.languageForm.markAllAsTouched();
      return;
    }
    this.languageService.updateLanguage(this.languageForm.value);
  }

  initiatForm() {
    this.languageForm = this.fb.group({
      languageCode: ['', Validators.required],
      name: ['', Validators.required],
      isDefault: [false],
    });
  }

  setForm(language: Language) {
    this.languageForm = this.fb.group({
      languageCode: [language.languageCode, Validators.required],
      name: [language.name, Validators.required],
    });
  }
}
