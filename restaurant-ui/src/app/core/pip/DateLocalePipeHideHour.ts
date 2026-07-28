import { Pipe, PipeTransform } from '@angular/core';
import { TranslateService } from '../service/translate.service';

@Pipe({
  name: 'dateLocalePipeHideHour',
  standalone:false
})
export class DateLocalePipeHideHour implements PipeTransform {

  constructor(private translate: TranslateService) {}

  transform(value: string): string {
    const utcDate = new Date(value);
    const localDate = new Date(utcDate.getTime() - (utcDate.getTimezoneOffset() * 60000));

    const months = this.translate.instant('months');


    const day = localDate.getDate();
    const month = months[localDate.getMonth()];
    const year = localDate.getFullYear();

    return `${day} ${month} ${year} `;
  }
}
