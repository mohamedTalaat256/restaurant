import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, computed, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { ProgressBarModule } from 'primeng/progressbar';
import { MessageService } from 'primeng/api';
import { Toast } from "primeng/toast";
import { ApplicationSettingService } from './application-setting.service';
import { CurrencyService } from '../currencies/currency.service';
import { LanguageService } from '../languages/language.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { ApplicationSetting } from '../../../../core/model/application-setting.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';

@Component({
  selector: 'app-application-settings',
  imports: [CommonModule, ButtonModule,
    InputTextModule, SelectModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanEditDirective],
  templateUrl: './application-settings.html',
  styleUrls: ['./application-settings.scss'],
  providers: [MessageService, ApplicationSettingService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class ApplicationSettings implements OnInit {


  settingService = inject(ApplicationSettingService);
  currencyService = inject(CurrencyService);
  languageService = inject(LanguageService);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'application_settings')?.id || 0;

  iconFile: File | null = null;
  logoFile: File | null = null;


  currencyOptions = computed(() => {
    return this.currencyService.currencies().map(c => ({ label: c.name + ' (' + c.symbol + ')', value: c.id }));
  });

  languageOptions = computed(() => {
    return this.languageService.languages().map(l => ({ label: l.name, value: l.languageCode }));
  });

  discountTypeOptions = [
    { label: 'Percentage', value: 'PERCENTAGE' },
    { label: 'Fixed Amount', value: 'FIXED_AMOUNT' }
  ];

  serviceChargeTypeOptions = [
    { label: 'Percentage', value: 'PERCENTAGE' },
    { label: 'Fixed Amount', value: 'FIXED_AMOUNT' }
  ];

  directionOptions = [
    { label: 'LTR', value: 'LTR' },
    { label: 'RTL', value: 'RTL' }
  ];

  ngOnInit(): void {
    this.settingService.initiatForm();
    this.settingService.loadSetting();
    this.currencyService.loadCurrencies();
    this.languageService.loadLanguages();

    // Watch for setting loaded
    /* const interval = setInterval(() => {
      const setting = this.settingService.setting();
      if (setting) {
        this.setForm(setting);
        clearInterval(interval);
        this.cdr.markForCheck();
      }
    }, 200); */
  }

  saveSetting() {
    if (this.settingService.settingForm.invalid) {
      this.settingService.settingForm.markAllAsTouched();
      return;
    }

    this.settingService.saveSetting(this.iconFile, this.logoFile);
  }



  onIconSelected(event: any) {
    this.iconFile = (event.target as HTMLInputElement)?.files?.[0] || null;
    if (this.iconFile) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.settingService.iconPreview.set(e.target.result);
        this.cdr.markForCheck();
      };
      reader.readAsDataURL(this.iconFile);
    }
  }

  onLogoSelected(event: any) {
    this.logoFile = (event.target as HTMLInputElement)?.files?.[0] || null;
    if (this.logoFile) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.settingService.logoPreview.set(e.target.result);
        this.cdr.markForCheck();
      };
      reader.readAsDataURL(this.logoFile);
    }
  }

  clearIcon() {
    this.iconFile = null;
    this.settingService.iconPreview.set('/images/person.jpg');
    const fileInput = document.getElementById('iconInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
    this.cdr.markForCheck();
  }

  clearLogo() {
    this.logoFile = null;
    this.settingService.logoPreview.set('/images/person.jpg');
    const fileInput = document.getElementById('logoInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
    this.cdr.markForCheck();
  }
}
