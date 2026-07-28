import { Directive, HostListener, inject } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[appArabicToEnglishNumber]',
  standalone:false
})
export class ArabicToEnglishNumberDirective {

  private ngControl = inject(NgControl);

  @HostListener('input', ['$event'])
  onInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const convertedValue = this.convertArabicToEnglish(input.value);
    this.ngControl.control?.setValue(convertedValue, { emitEvent: false });
  }

  private convertArabicToEnglish(value: string): string {
    const arabicNumbers = ['٠','١','٢','٣','٤','٥','٦','٧','٨','٩'];

    return value
      .split('')
      .map(char => {
        const index = arabicNumbers.indexOf(char);
        return index !== -1 ? index.toString() : char;
      })
      .join('');
  }
}
