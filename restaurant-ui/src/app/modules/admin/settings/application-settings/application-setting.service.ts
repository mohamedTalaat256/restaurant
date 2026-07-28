import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { ApplicationSetting } from '../../../../core/model/application-setting.model';
import { env } from '../../../../../environment/env';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class ApplicationSettingService {

  settingForm!: FormGroup;

  setting = signal<ApplicationSetting | null>(null);
  loading = signal(false);
  loadingSave = signal(false);
  savedSuccess = signal(false);

  iconPreview = signal<string | ArrayBuffer | null>('/images/person.jpg');
  logoPreview = signal<string | ArrayBuffer | null>('/images/person.jpg');

  appLogo = signal<string | null>('/images/person.png');
  appIcon = signal<string | null>('/images/fav.ico');
  appDirection = signal<string>('LTR');
  imagesUrl = env.baseUrl;

  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  readonly translate = inject(TranslateService);
  readonly fb = inject(FormBuilder);

  loadSetting() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<ApplicationSetting>>(env.apiUrl + '/settings/application-settings').subscribe({
      next: (res) => {
        this.setting.set(res.data);
        this.setForm(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  saveSetting(iconFile: File | null, logoFile: File | null) {
    this.loadingSave.set(true);
    this.error.set(null);

    const formData = this.toFormData(this.settingForm.value, iconFile, logoFile);

    this.http.put<any>(env.apiUrl + '/settings/application-settings', formData).subscribe({
      next: (res: ApiResponse<ApplicationSetting>) => {
        if (res.status) {
          this.setting.set(res.data);
          this.loadingSave.set(false);
          this.savedSuccess.set(true);
          this.setForm(res.data);

          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('label_successful'),
            detail: this.translate.instant(res.message),
            life: 3000
          });
        } else {
          this.loadingSave.set(false);
          this.error.set(res.message);
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant('label_failed'),
            detail: this.translate.instant(res.message),
            life: 3000
          });
        }
      },
      error: () => {
        this.loadingSave.set(false);
      }
    });
  }

  private toFormData(formValue: any, iconFile: File | null, logoFile: File | null): FormData {
    const formData = new FormData();

    Object.keys(formValue).forEach((key) => {
      if (key === 'icon' || key === 'logo') {
        return;
      }

      const value = formValue[key];
      if (value === null || value === undefined) {
        return;
      }

      formData.append(key, String(value));
    });

   // formData.append('data', JSON.stringify(formValue));

    if (iconFile) {
      formData.append('iconFile', iconFile);
    }

    if (logoFile) {
      formData.append('logoFile', logoFile);
    }

    return formData;
  }






  initiatForm() {
    this.settingForm = this.fb.group({
      id: [null],
      applicationTitle: ['', Validators.required],
      storeName: ['', Validators.required],
      address: [''],
      phone: [''],
      icon: [''],
      logo: [''],
      openingTime: [''],
      closingTime: [''],
      discountType: ['PERCENTAGE'],
      discountPercentage: [null],
      serviceChargeType: ['PERCENTAGE'],
      taxPercentage: [null],
      taxNumber: [''],
      currencyId: [null],
      languageCode: [null],
      dateFormat: [''],
      timezone: [''],
      applicationDirection: ['LTR'],
      poweredByText: [''],
      footerText: [''],
    });
  }

  setForm(setting: ApplicationSetting) {
    this.appIcon.set(setting.icon ? this.imagesUrl + setting.icon : '/images/fav.ico');
    this.appLogo.set(setting.logo ? this.imagesUrl + setting.logo : '/images/logo.png');
    this.appDirection.set(setting.applicationDirection || 'LTR');
    this.translate.setLocale(setting.languageCode || 'en');

    this.settingForm = this.fb.group({
      id: [setting.id],
      applicationTitle: [setting.applicationTitle, Validators.required],
      storeName: [setting.storeName, Validators.required],
      address: [setting.address],
      phone: [setting.phone],
      icon: [setting.icon],
      logo: [setting.logo],
      openingTime: [setting.openingTime],
      closingTime: [setting.closingTime],
      discountType: [setting.discountType],
      discountPercentage: [setting.discountPercentage],
      serviceChargeType: [setting.serviceChargeType],
      taxPercentage: [setting.taxPercentage],
      taxNumber: [setting.taxNumber],
      currencyId: [setting.currency?.id || setting.currencyId],
      languageCode: [ setting.languageCode],
      dateFormat: [setting.dateFormat],
      timezone: [setting.timezone],
      applicationDirection: [setting.applicationDirection],
      poweredByText: [setting.poweredByText],
      footerText: [setting.footerText],
    });

    if (setting.icon) {
      this.iconPreview.set(this.imagesUrl + setting.icon);
    }
    if (setting.logo) {
      this.logoPreview.set(this.imagesUrl + setting.logo);
    }

    localStorage.setItem('applicationSettings', JSON.stringify(setting));
  }

  loadSettingFromLocalStorage() {
    const stored = localStorage.getItem('applicationSettings');
    if (!stored) return;
    try {
      const setting: ApplicationSetting = JSON.parse(stored);
      this.setting.set(setting);
      this.setForm(setting);
    } catch (error) {
      this.loadSetting();
    }
  }
}
