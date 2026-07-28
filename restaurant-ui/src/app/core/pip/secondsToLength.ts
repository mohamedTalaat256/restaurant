import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'secondsToLength',
  standalone:false
})
/*
export class secondsToLength implements PipeTransform {
  transform(minutes: number): string {
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;
    const seconds = remainingMinutes * 60;


    var str= '';
    if(remainingMinutes ===0 ){
      return `${hours} h`;
    }else if(hours == 0){
      return ` ${remainingMinutes} min `;
    }else{
      return `${hours} h ${remainingMinutes} min `;
    }
  }
}
 */
export class secondsToLength implements PipeTransform {
  transform(seconds: number): string {
    const hrs = Math.floor(seconds / 3600);
    const mins = Math.floor((seconds % 3600) / 60);
    const secs = Math.floor(seconds % 60);

    return (
      (hrs > 0 ? hrs.toString().padStart(2, '0') + ':' : '') +
      mins.toString().padStart(2, '0') + ':' +
      secs.toString().padStart(2, '0')
    );
  }
}
