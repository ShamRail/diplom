import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { Language, RESTLanguageService } from '../../services/RESTLanguageService';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-language-form',
  templateUrl: './language-form.component.html',
  styleUrls: ['./language-form.component.css']
})
export class LanguageFormComponent implements OnInit {

  form: FormGroup;
  @Output() save: EventEmitter<Language> = new EventEmitter<Language>();

  constructor(private languageCrudService: RESTLanguageService) {
    this.form = new FormGroup({
      name: new FormControl('', Validators.required),
      version: new FormControl('', Validators.required),
      file: new FormControl('')
    });
  }

  ngOnInit(): void { }

  onSubmit(): void {

    const formData = new FormData();
    formData.append('name', this.form.value.name);
    formData.append('version', this.form.value.version);
    formData.append('file', this.form.value.file);

    this.languageCrudService.saveFormData(formData).subscribe((response) => {
      const generatedId = response.id;
      const language = new Language(this.form.value.name, this.form.value.version);
      language.id = generatedId;
      language.logo = response.logo;
      this.save.emit(language);
    });

  }
  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    if (control !== undefined && control !== null && !control.valid) {
      return true;
    }
    return false;
  }

  onFileValueChanged(event: any): void {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      const formControl = this.form.get('file');
      if (formControl != null) {
        formControl.setValue(file);
      }
    }
  }

}
