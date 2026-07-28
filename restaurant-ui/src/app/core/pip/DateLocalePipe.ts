import { Pipe, PipeTransform } from '@angular/core';
import { TranslateService } from '../service/translate.service';

@Pipe({
  name: 'dateLocale',
  standalone:false
})
export class DateLocalePipe implements PipeTransform {

  constructor(private translate: TranslateService) {}

  transform(value: string): string {
    const utcDate = new Date(value);
    const localDate = utcDate;//new Date(utcDate.getTime() - (utcDate.getTimezoneOffset() * 60000));

    const months = this.translate.instant('months');
    const amText = this.translate.instant('am');
    const pmText = this.translate.instant('pm');

    const day = localDate.getDate();
    const month = months[localDate.getMonth()];
    const year = localDate.getFullYear();
    let hours = localDate.getHours();
    const minutes = localDate.getMinutes();
    const ampm = hours >= 12 ? pmText : amText;

    hours = hours % 12 || 12;
    return `${day} ${month} ${year}, ${this.translate.instant('the_hour')} ${hours}:${minutes < 10 ? '0' : ''}${minutes} ${ampm}`;
  }
}
