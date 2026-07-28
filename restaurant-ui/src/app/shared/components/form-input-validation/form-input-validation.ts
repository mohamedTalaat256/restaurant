import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { TranslateService } from '../../../core/service/translate.service';

@Component({
  selector: 'app-form-input-validation',
  imports: [],
  templateUrl: './form-input-validation.html',
  styleUrl: './form-input-validation.scss',
})
export class FormInputValidation implements OnInit {
  @Input() control!: AbstractControl | null;
  @Input() fieldName!: string;
  @Input() patternEx!: string;

  constructor(private translate: TranslateService) { }

  ngOnInit() { }

  // Helper method to get the first error message
  getErrorMessage(): string {
    if (!this.control || !this.control.errors) return '';

    const errors = this.control.errors;

    if (errors['required']) {
      return this.translate.instant('validation.required', { field: this.translate.instant(this.fieldName) });
    }

    if (errors['minlength']) {
      return this.translate.instant('validation.minlength', {
        field: this.translate.instant(this.fieldName),
        length: errors['minlength'].requiredLength
      });
    }

    if (errors['maxlength']) {
      return this.translate.instant('validation.maxlength', {
        field: this.translate.instant(this.fieldName),
        length: errors['maxlength'].requiredLength
      });
    }

    if (errors['min']) {
      return this.translate.instant('validation.min', {
        field: this.translate.instant(this.fieldName),
        min: errors['min'].min
      });
    }

    if (errors['max']) {
      return this.translate.instant('validation.max', {
        field: this.translate.instant(this.fieldName),
        max: errors['max'].max
      });
    }

    if (errors['email']) {
      return this.translate.instant('validation.email', { field: this.translate.instant(this.fieldName) });
    }

    if (errors['pattern']) {
      return this.patternEx || this.translate.instant('validation.pattern', { field: this.translate.instant(this.fieldName) });
    }

    return this.translate.instant('validation.invalid', { field: this.translate.instant(this.fieldName) });
  }
}
