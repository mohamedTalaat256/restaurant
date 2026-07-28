import { Pipe, PipeTransform } from '@angular/core';
import {TranslateService} from '@ngx-translate/core';

@Pipe({
  name: 'houreConverter',
  standalone:false
})
export class HoureConverter implements PipeTransform {

  constructor(private translate: TranslateService) {
   }

  transform(value: string): string {
    let date: Date;

    // Check if the input is a time-only string (e.g., "03:00:00")
    if (value.match(/^\d{2}:\d{2}:\d{2}$/)) {
      // If it's a time-only string, create a date object with today's date and the provided time
      const [hours, minutes, seconds] = value.split(':').map(Number);
      date = new Date();
      date.setHours(hours, minutes, seconds);
    } else {
      // Otherwise, treat it as a full date-time string
      date = new Date(value);
    }

    // Get the hour part in 12-hour format
    let hours = date.getHours();
    const ampm = hours >= 12 ? this.translate.instant('pm') : this.translate.instant('am');
    hours = hours % 12;
    hours = hours ? hours : 12; // if hour is 0, make it 12

    // Return the formatted hour with AM/PM
    return `${hours} ${ampm}`;
  }
}
