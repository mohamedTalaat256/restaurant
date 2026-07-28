import { DatePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';
import { LOOKUPS } from '../data/lookups';

@Pipe({
  name: 'getLooupValue',
  standalone:false
})
export class GetLooupValue implements PipeTransform {

  constructor() { }

  transform(key: number, type: string): string {
    let lookup = LOOKUPS.find(row => { return  row.type === type && row.key === key });
    if(lookup ){
      return lookup.value;
    }else{
      return ''
    }

  }
}
