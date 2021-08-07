import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Language, RESTLanguageService } from '../../services/RESTLanguageService';
import { BuilderIn, BuilderOut, RESTBuilderService } from '../../services/RESTBuilderService';

@Component({
  selector: 'app-builder-edit-form',
  templateUrl: './builder-edit-form.component.html',
  styleUrls: ['./builder-edit-form.component.css']
})
export class BuilderEditFormComponent implements OnInit {

  form: any;
  builder: BuilderOut = new BuilderOut();
  languages: Language[] = [];

  constructor(
    private languageCrudService: RESTLanguageService,
    private builderService: RESTBuilderService,
    private activatedRoute: ActivatedRoute,
    private route: Router
  ) {
    this.form = new FormGroup({
      name: new FormControl('', Validators.required),
      version: new FormControl('', Validators.required),
      languages: new FormControl('', Validators.required)
    });
  }

  ngOnInit(): void {
    this.languageCrudService.findAll()
      .subscribe((resp) => {
        this.languages = resp;
      });
    this.activatedRoute.params.subscribe((params: Params) => {
        this.builderService.findById(+params.id)
          .subscribe((resp) => {
            this.builder = resp;
            this.form = new FormGroup({
              name: new FormControl(this.builder.name, Validators.required),
              version: new FormControl(this.builder.version, Validators.required),
              languages: new FormControl(this.builder.languages.map(l => l.id), Validators.required)
            });
          }, (error) => {
            this.route.navigate(['/builders']);
          });
    });
  }

  onSubmit(): void {
    this.builderService.update(this.builder.id, new BuilderIn(
      this.form.value.name, this.form.value.version, this.form.value.languages
    )).subscribe((resp) => {
      this.route.navigate(['/builders']);
    });
  }

  contains(id: number): boolean {
    return this.builder.languages
      .filter(l => l.id === id)
      .length !== 0;
  }

  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    if (field !== undefined && field !== null && !control.valid) {
      return true;
    }
    return false;
  }
}
