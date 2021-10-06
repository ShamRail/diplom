import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Language, RESTLanguageService} from '../../services/RESTLanguageService';
import {BuilderIn, BuilderOut, RESTBuilderService} from '../../services/RESTBuilderService';

@Component({
  selector: 'app-builder-form',
  templateUrl: './builder-form.component.html',
  styleUrls: ['./builder-form.component.css']
})
export class BuilderFormComponent implements OnInit {

  form: any;
  languages: Language[] = [];
  selectSize = 1;
  uploadFile: any;

  @Output() onAdd: EventEmitter<BuilderOut> = new EventEmitter<BuilderOut>();

  constructor(
    private languageCrudService: RESTLanguageService,
    private builderService: RESTBuilderService) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      name: new FormControl('', Validators.required),
      version: new FormControl('', Validators.required),
      languages: new FormControl('', Validators.required),
      file: new FormControl()
    });
    this.languageCrudService.findAll()
      .subscribe((langs) => {
        this.languages = langs;
      });
  }
  onSubmit(): void {

    const formData = new FormData();
    formData.append('name', this.form.value.name);
    formData.append('version', this.form.value.version);
    formData.append('languageID', this.form.value.languages);
    formData.append('file', this.uploadFile);

    this.builderService.saveByFormData(formData)
      .subscribe((resp) => {
          this.form.reset();
          this.onAdd.emit(resp);
        }
      );
  }
  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    if (field !== undefined && field !== null && !control.valid) {
      return true;
    }
    return false;
  }

  onFileValueChanged(event: any): void {
    if (event.target.files.length > 0) {
      this.uploadFile = event.target.files[0];
    }
  }

}
