import { Pipe, PipeTransform } from '@angular/core';
import { TranslateService } from '../service/translate.service';

@Pipe({
  name: 'dateLocalePipeHideDate',
  standalone:false
})
export class DateLocalePipeHideDate implements PipeTransform {

  constructor(private translate: TranslateService) {}

  transform(value: string): string {
    const utcDate = new Date(value);
    const localDate = new Date(utcDate.getTime() - (utcDate.getTimezoneOffset() * 60000));

    const amText = this.translate.instant('am');
    const pmText = this.translate.instant('pm');


    let hours = localDate.getHours();
    const minutes = localDate.getMinutes();
    const ampm = hours >= 12 ? pmText : amText;

    hours = hours % 12 || 12;
    return ` ${hours}:${minutes < 10 ? '0' : ''}${minutes} ${ampm}`;
  }
}
