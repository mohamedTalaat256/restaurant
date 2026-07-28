import { DatePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateConverter',
  standalone:false
})
export class DateConverter implements PipeTransform {

  constructor(private datePipe: DatePipe) { }

  transform(date: string): string {
    return ' '+this.datePipe.transform(date, 'yyyy-MM-dd')+' ';

  }
}
