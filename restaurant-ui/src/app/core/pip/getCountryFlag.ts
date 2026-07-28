import { DatePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';
import { COUNTRIES } from '../data/countries';

@Pipe({
  name: 'getCountryFlag',
  standalone:false
})
export class GetCountryFlag implements PipeTransform {

  constructor() { }

  transform( country: string): string {
    let lookup = COUNTRIES.find(row => row.name.toLocaleLowerCase() === country.toLocaleLowerCase());
    if (lookup) {
      return lookup.image;
    } else {
      return 'https://cdn.jsdelivr.net/npm/country-flag-emoji-json@2.0.0/dist/images/EG.svg';
    }
  }
}
