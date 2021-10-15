import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {App, AppService} from "../services/AppService";
import {AbstractControl, FormControl, FormGroup} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {PathValidator} from "../validators/PathValidator";

export class BufferDTO {
  file: string = '';
  blob: Blob = new Blob();
}

@Component({
  selector: 'app-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.css']
})
export class PanelComponent implements OnInit {

  @Input() app: App = new App();
  @Output() onRun: EventEmitter<string> = new EventEmitter<string>();
  @Output() onPreview: EventEmitter<BufferDTO> = new EventEmitter<BufferDTO>();
  @Output() onInput: EventEmitter<any> = new  EventEmitter<any>();
  @Output() onStop: EventEmitter<any> = new  EventEmitter<any>();
  @Output() onStart: EventEmitter<any> = new EventEmitter<any>();

  inFiles: string[] = [];
  outFiles: string[] = [];

  inForms: FormGroup[] = [];
  uploadAnyForm: FormGroup = new FormGroup({});
  downLoadAny: FormGroup = new FormGroup({});

  constructor(private appService: AppService) {
    this.uploadAnyForm = new FormGroup({
      path: new FormControl('someDir/file.txt', PathValidator.correctWay),
      file: new FormControl()
    });
    this.downLoadAny = new FormGroup({
      path: new FormControl('someDir/file.txt', PathValidator.correctWay)
    });
  }

  ngOnInit(): void {
    this.initForms(this.inFiles, this.inForms);
    this.initOutFiles();
  }

  private initForms(dest: string[], forms: FormGroup[]): void {
    let files = PanelComponent.filterFiles(this.app.project.inFiles);
    for (let i = 0; i < files.length; i++) {
      dest.push(files[i]);
      forms.push(new FormGroup({
        path: new FormControl(files[i]),
        file: new FormControl()
      }));
    }
  }

  private initOutFiles() {
    PanelComponent.filterFiles(this.app.project.outFiles).forEach(s => this.outFiles.push(s));
  }

  onFileValueChanged(event: any, index?: number) {
    let formGroup = this.uploadAnyForm;
    if (index != undefined) {
      formGroup = this.inForms[index];
    }
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      let formControl = formGroup.get('file');
      if (formControl != null) {
        formControl.setValue(file);
      }
    }
  }

  onClickUpload(index?: number) {
    let formGroup = this.uploadAnyForm;
    if (index != undefined) {
      formGroup = this.inForms[index];
    }
    let fileControl = formGroup.get('file');
    let pathControl = formGroup.get('path');
    if (fileControl != null && pathControl != null && fileControl.value && this.uploadAnyForm.valid) {
      const formData = new FormData();
      formData.append('file', fileControl.value);
      formData.append('filePath', pathControl.value);
      this.appService.uploadFile(this.app.id, formData).subscribe(
        (res) => alert('Файл успешно загружен!'),
        (err) => {
          alert('При загрузке файла произошла ошибка. Проверьте путь и размер файла. Размер файла должен быть < 5MB');
          console.log(err);
        }
      );
    } else {
      alert('Укажите корректный путь и выберите файл');
    }
  }

  onClickDownload(file: string, isAny?: boolean): void {
    if (isAny != undefined && !this.downLoadAny.valid) {
      alert('Пожалуйста укажите корреткный путь');
      return;
    }
    this.appService.downloadFile(this.app.id, file)
      .subscribe(
        data => {
          let link = document.createElement('a');
          link.download = this.getFileName(file);
          link.href = URL.createObjectURL(data);
          link.click();
          URL.revokeObjectURL(link.href);
        },
        (error: HttpErrorResponse) => {
          if (!error.ok) {
            alert('Произошла ошибка при загрузке файла. Проверьте корректность путей');
            console.log(error);
          }
        }
      )
  }

  onClickPreview(file: string, isAny?: boolean): void {
    if (isAny != undefined && !this.downLoadAny.valid) {
      alert('Пожалуйста укажите корреткный путь');
      return;
    }
    this.appService.downloadFile(this.app.id, file)
      .subscribe(
        data => {
          this.onPreview.emit({file: file, blob: data});
        },
        (error: HttpErrorResponse) => {
          if (!error.ok) {
            alert('Произошла ошибка при загрузке файла. Проверьте корректность путей');
            console.log(error);
          }
        }
      )
  }

  onClickRunCommand() {
    this.onRun.emit(this.app.project.runCommand);
  }

  private static filterFiles(source: string): string[] {
    return source.split(';')
      .filter(s => s.length != 0)
      .map(s => s.trim())
      .map(s => s.startsWith("/") ? s.substr(1) : s);
  }

  getFileName(path: string): string {
    let lastSlashPosition = path.lastIndexOf('/');
    return path.substring(lastSlashPosition + 1);
  }

  disableTerminal() {
    this.onInput.emit();
  }

  restart() {
    let result = confirm('Вы действительно хотите перезапустить приложение? Все изменения будут потеряны');
    if (result) {
      window.location.reload(false);
    }
  }

  stop() {
    let result = confirm('Вы действительно хотите остановить приложение? Все изменения будут потеряны');
    if (result) {
      this.app.isActive = false;
      this.onStop.emit();
    }
  }

  start() {
    this.app.isActive = false;
    this.onStart.emit();
  }

  updateApp(event: App) {
    this.app = event;
  }

  isInvalid(control: AbstractControl | null): boolean {
    let result = false;
    if (control != undefined && control.touched && control.invalid) {
      result = true;

    }
    return result;
  }

}
