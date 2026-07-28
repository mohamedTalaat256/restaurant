import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateHoureConverter',
  standalone:false
})
export class DateHoureConverter implements PipeTransform {

  constructor( ) {
   }

  transform(value: string): string {
      // Parse the input string as a Date
      const date = new Date(value);

      // Get the hour part in 12-hour format
      let hours = date.getHours();
      const ampm = hours >= 12 ? 'PM' : 'AM';
      hours = hours % 12;
      hours = hours ? hours : 12; // if hour is 0, make it 12

      // Return the formatted hour with AM/PM
      return `${hours} ${ampm}`;
  }
}
