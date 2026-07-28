import { Pipe, PipeTransform } from '@angular/core';
import { COUNTRIES } from '../data/countries';

@Pipe({
  name: 'getCountryLookup',
  standalone:false
})
export class GetCountryLooup implements PipeTransform {

  constructor() { }

  transform(languageCode: string ): string {
    let lookup = COUNTRIES.find(row => { return  row.code === languageCode });
    if(lookup ){
      return lookup.name;
    }else{
      return ''
    }

  }
}
