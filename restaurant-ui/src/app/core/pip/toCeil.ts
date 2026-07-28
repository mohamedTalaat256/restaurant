import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'toCeil',
  standalone:false})
export class toCeil implements PipeTransform {
  transform(value: number): number {
    return Math.ceil(value)
  }
}
