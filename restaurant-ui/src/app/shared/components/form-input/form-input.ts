import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { NgTemplateOutlet } from '@angular/common';
import { ControlContainer, FormControl, FormGroupDirective, FormsModule, NgControl, ReactiveFormsModule } from '@angular/forms';
import { RippleModule } from 'primeng/ripple';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { Select } from "primeng/select";
import { RadioButton } from "primeng/radiobutton";
import { IconField } from "primeng/iconfield";
import { InputIcon } from "primeng/inputicon";
import { FloatLabelModule } from 'primeng/floatlabel';
import { MultiSelect } from "primeng/multiselect";
import { TranslateService } from '../../../core/service/translate.service';
import { Message } from "primeng/message";
import { DatePickerModule } from 'primeng/datepicker';

@Component({
  selector: 'app-form-input',
  standalone: true,
  imports: [
    NgTemplateOutlet,
    ReactiveFormsModule,
    ButtonModule, CheckboxModule, InputTextModule, FormsModule, RouterModule, RippleModule,
    FloatLabelModule,
    Select,
    RadioButton,
    IconField,
    InputIcon,
    MultiSelect,
    DatePickerModule,
    Message,

  ],
  templateUrl: './form-input.html',
  styles: [`
      .input-icon{
        padding-inline-start: 2.5rem !important;
        padding-inline-end: 12px !important;
      }
      .custom-form-label{
        font-size: 0.95rem !important;
        font-weight: 500 !important;
        color: #333 !important;
        display: flex !important;
        justify-content: flex-start;
        align-items: center;
        gap: 0.25rem;
        width: 100%;
        text-align: start;
      }
      .label-on{
        width: fit-content;
      }

    `],
  viewProviders: [{ provide: ControlContainer, useExisting: FormGroupDirective }]

})
export class FormInput implements OnInit {
  @Input({ required: true }) control!: FormControl;
  @Input({ required: true }) label!: string;
  @Input() hideLabel: boolean = false;
  @Input() filter: boolean = false;


  @Input() labelVarient: 'over' | 'on' | 'in' = 'over';
  @Input() requiredAstric: boolean = true;
  @Input() placeholder: string = '';
  @Input() type: string = 'text';
  @Input() icon?: string;
  @Input() options?: any[];
  @Input() fieldName: string = ''; // Key for the translation file
  @Input() patternEx?: string;    // Custom pattern message
   @Input() loading: boolean = false;
  @Output() valueChanged = new EventEmitter<any>();

  translate = inject(TranslateService);
  currentDirection: 'rtl' | 'ltr' = 'ltr';

  ngOnInit() {

    this.currentDirection = this.translate.currentLocale() === 'en' ? 'ltr' : 'rtl';
    this.control.valueChanges.subscribe(value => {
      this.valueChanged.emit(value);
    });
  }

  getErrorMessage(): string {
    const errors = this.control?.errors;

    if (!errors || !this.control?.touched) {
      return '';
    }

    const field = this.translate.instant(this.label);

    if (errors['required']) {
      return `${field} ${this.translate.instant('error_required')}`;
    }

    if (errors['minlength']) {
      return `${field} ${this.translate.instant('error_min_length_is', {
        min: errors['minlength'].requiredLength
      })}`;
    }

    if (errors['maxlength']) {
      return `${field} ${this.translate.instant('error_max_length_is', {
        max: errors['maxlength'].requiredLength
      })}`;
    }

    if (errors['min']) {
      return `${field} ${this.translate.instant('error_min_value_is', {
        min: errors['min'].min
      })}`;
    }

    if (errors['max']) {
      return `${field} ${this.translate.instant('error_max_value_is', {
        max: errors['max'].max
      })}`;
    }

    if (errors['email']) {
      return this.translate.instant('error_enter_valid_email');
    }

    if (errors['pattern']) {
      return this.patternEx || this.translate.instant('error_pattern');
    }

    return this.translate.instant('error_invalid');
  }
  isRequired(): boolean {
    const errors = this.control?.errors;
    return !!(errors && errors['required']);
  }



  onInput(event: any) {
    let value = event.target.value;

    if (this.type === 'number') {
      value = value === '' ? null : Number(value);
    }

    this.control.setValue(value);
    this.valueChanged.emit(value); // 🔥 emit to parent
  }
}
