import { Component, Input, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { Language, RESTLanguageService } from '../../services/RESTLanguageService';

@Component({
  selector: 'app-language-form',
  templateUrl: './language-form.component.html',
  styleUrls: ['./language-form.component.css']
})
export class LanguageFormComponent implements OnInit {

  @Input() languages: Language[] = [];
  form: FormGroup;

  constructor(private languageCrudService: RESTLanguageService) {
    this.form = new FormGroup({
      name: new FormControl('', Validators.required),
      version: new FormControl('', Validators.required)
    });
  }

  ngOnInit(): void { }

  onSubmit(): void {
    const language = new Language(this.form.value.name, this.form.value.version);
    this.languageCrudService.save(language).subscribe((resp) => {
        language.id = resp.id;
        console.log(resp);
        this.languages.push(language);
    });
  }
  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    if (control !== undefined && control !== null && !control.valid) {
      return true;
    }
    return false;
  }

}
