import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, computed, inject, OnInit, ViewChild } from '@angular/core';
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
import { LanguageTranslationService } from './language-translation.service';
import { LanguageService } from '../languages/language.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { LanguageTranslation } from '../../../../core/model/language-translation.model';
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
  selector: 'app-language-translations',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './language-translations.html',
  styleUrls: ['./language-translations.scss'],
  providers: [MessageService, ConfirmationService, LanguageTranslationService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class LanguageTranslations implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];

  translationForm!: FormGroup;

  translationService = inject(LanguageTranslationService);
  languageService = inject(LanguageService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'language_translations')?.id || 0;

  languageOptions = computed(() => {
    return this.languageService.languages().map(lang => ({ label: lang.name, value: lang.languageCode }));
  });

  ngOnInit(): void {
    this.translationService.loadTranslations();
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
    this.translationService.translationDialog.set(true);
  }

  editTranslation(translation: LanguageTranslation) {
    this.translationService.translationDialog.set(true);
    this.setForm(translation);
  }

  hideDialog() {
    this.translationService.translationDialog.set(false);
  }

  deleteTranslation(translation: LanguageTranslation) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_language_translation'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.translationService.deleteTranslation(translation.id);
      }
    });
  }

  saveTranslation() {
    if (this.translationForm.invalid) {
      this.translationForm.markAllAsTouched();
      return;
    }

    if (this.translationForm.value.id) {
      this.translationService.updateTranslation(this.translationForm.value);
    } else {
      this.translationService.saveTranslation(this.translationForm.value);
    }
  }

  initiatForm() {
    this.translationForm = this.fb.group({
      id: [null],
      languageCode: [null, Validators.required],
      key: ['', Validators.required],
      value: ['', Validators.required],
    });
  }

  setForm(translation: LanguageTranslation) {
    this.translationForm = this.fb.group({
      id: [translation.id],
      languageCode: [translation.languageCode , Validators.required],
      key: [translation.key, Validators.required],
      value: [translation.value, Validators.required],
    });
  }
}
