import {AbstractControl} from "@angular/forms";

export class PathValidator {
  static correctWay(control: AbstractControl): {[key: string]: boolean} | null {
    let path: string = control.value;
    let lastSlashIndex = path.lastIndexOf('/');
    if (path.startsWith("/") || lastSlashIndex == -1 || path.substring(lastSlashIndex + 1).length == 0) {
      console.log('way invalid ', path);
      return {
        'correctWay': true
      }
    }
    return null;
  }
}
