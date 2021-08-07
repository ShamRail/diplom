import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'time'
})
export class TimePipe implements PipeTransform {

  transform(value: number[], ...args: unknown[]): string {
    let current: Date = new Date(
      Date.UTC(
        value[0], value[1], value[2],
        value[3], value[4], value[5], 0)
    );
    return `${current.getHours()}:${current.getMinutes()}:${current.getSeconds()} ${current.getDate()}.${current.getMonth()}.${current.getFullYear()}`;
  }

}
