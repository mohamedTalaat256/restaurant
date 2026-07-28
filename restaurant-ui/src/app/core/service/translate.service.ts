import { Injectable, signal, effect, inject } from '@angular/core';
import { MessageService } from 'primeng/api';
import { PrimeNG } from 'primeng/config';
import { arDatePickerConfig, enDatePickerConfig } from '../config/date-picker.configuration';

interface Message {
  severity: 'success' | 'error' | 'info' | 'warning';
  detail: string;
  summary?: string;
}

@Injectable({ providedIn: 'root' })
export class TranslateService {
  private _locale = signal<string>('ar');
  private messages = signal<Record<string, string>>({});
  private displayedMessage = signal<Message | null>(null);
  private readonly MESSAGE_TIMEOUT_MS = 4000;

  readonly currentLocale = this._locale.asReadonly();
  readonly message = this.displayedMessage.asReadonly();

  private messageService = inject(MessageService);
  private primengConfig = inject(PrimeNG);
  private styleId = 'app-lang-style';

  private dirEffect = effect(() => {
    const lang = this._locale();
    document.documentElement.lang = lang;
    document.documentElement.dir = lang === 'ar' ? 'rtl' : 'ltr';

    const head = document.head;
    let link = document.getElementById(this.styleId) as HTMLLinkElement;

    if (!link) {
      link = document.createElement('link');
      link.id = this.styleId;
      link.rel = 'stylesheet';
      head.appendChild(link);
    }

    link.href = lang === 'ar'
      ? 'public/css/stylesAr.scss'
      : 'public/css/stylesEn.scss';

  });

  async setLocale(locale: string): Promise<void> {
    const res = await fetch(`/i18n/${locale}.json`);
    if (!res.ok) return;
    const data = await res.json();
    this._locale.set(locale);
    this.messages.set(data);
    this.primengConfig.setTranslation(this._locale() === 'ar' ? arDatePickerConfig: enDatePickerConfig);
  }

  instant(key: string, params?: Record<string, string>): string {
    let message = this.messages()[key] ?? key;
    if (params) {
      Object.entries(params).forEach(([key, value]) => {
        message = message.replace(`{{${key}}}`, value);
      });
    }
    return message;
  }

  // Show success message
  showSuccessMessage(messageKey: string): void {
    this.messageService.add({
      severity: 'success',
      detail: this.instant(messageKey),
      life: this.MESSAGE_TIMEOUT_MS
    });
  }

  // Show success message with parameters
  showParameterizedSuccessMessage(messageKey: string, params: Record<string, string>): void {
    this.messageService.add({
      severity: 'success',
      detail: this.instant(messageKey, params),
      life: this.MESSAGE_TIMEOUT_MS
    });
  }

  // Show error message
  showErrorMessage(messageKey: string, translate: boolean = true): void {
    this.messageService.add({
      severity: 'error',
      detail: translate ? this.instant(messageKey) : messageKey,
      life: this.MESSAGE_TIMEOUT_MS
    });
  }

  // Show error message with parameters
  showParameterizedErrorMessage(messageKey: string, params: Record<string, string>): void {
    this.messageService.add({
      severity: 'error',
      detail: this.instant(messageKey, params),
      life: this.MESSAGE_TIMEOUT_MS
    });
  }

  // Combine labels based on text direction
  getCombinedLabel(firstLabelKey: string, secondLabelKey: string): string {
    const isRtl = this._locale() === 'ar';
    const firstLabel = this.instant(firstLabelKey);
    const secondLabel = this.instant(secondLabelKey);
    return isRtl ? `${firstLabel} ${secondLabel}` : `${secondLabel} ${firstLabel}`;
  }

  // Get text based on current locale direction
  decideArabicOrEnglish(arabic: string, english: string): string {
    return this._locale() === 'ar' ? arabic : english;
  }
}
