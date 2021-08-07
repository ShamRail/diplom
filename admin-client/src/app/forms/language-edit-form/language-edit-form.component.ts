import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { Language, RESTLanguageService } from '../../services/RESTLanguageService';

@Component({
  selector: 'app-language-edit-form',
  templateUrl: './language-edit-form.component.html',
  styleUrls: ['./language-edit-form.component.css']
})
export class LanguageEditFormComponent implements OnInit {

  language: any = {name: 'stub', version: 'stub'};
  form: FormGroup;

  constructor(
    private languageCrudService: RESTLanguageService,
    private activatedRouter: ActivatedRoute,
    private router: Router
  ) {
    this.form = new FormGroup({
      name: new FormControl('', Validators.required),
      version: new FormControl('', Validators.required)
    });
  }

  ngOnInit(): void {
    this.activatedRouter.params
      .subscribe((params: Params) => {
        this.languageCrudService.findById(+params.id)
          .subscribe((resp) => {
            this.language = new Language(
              resp.name, resp.version, resp.id
            );
            this.form = new FormGroup({
              name: new FormControl(this.language.name, Validators.required),
              version: new FormControl(this.language.version, Validators.required)
            });
          });
      });
  }

  onSubmit(): void {
    this.languageCrudService.update(
      this.language.id, new Language(this.form.value.name, this.form.value.version)
    ).subscribe((resp) => {
      this.router.navigate(['/languages']);
    });
  }

  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    return control !== undefined && control !== null && !control.valid;
  }
}
