import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Language, RESTLanguageService } from '../services/RESTLanguageService';
import { BuilderOut, RESTBuilderService } from '../services/RESTBuilderService';
import { ConfigIn, RESTConfigService } from '../services/RESTConfigService';

@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
  styleUrls: ['./config.component.css']
})
export class ConfigComponent implements OnInit {

  form: any;
  config: any;
  editable = false;
  languageID: any;
  builderID: any;

  languages: Language[] = [];
  builders: BuilderOut[] = [];

  constructor(
    private languageService: RESTLanguageService,
    private builderService: RESTBuilderService,
    private configService: RESTConfigService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
    this.form = new FormGroup({
      name: new FormControl('', Validators.required),
      version: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      dockerFile: new FormControl('', Validators.required),
      language: new FormControl('', Validators.required),
      builder: new FormControl('', Validators.required)
    });
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => {

      this.config = this.configService.findById(+params.id)
        .subscribe((response) => {
          console.log(response);

          this.config = response;

          this.languageService.findAll().subscribe((r) => this.languages = r);
          this.builderService.findAll().subscribe((r) => this.builders = r);

          this.languageID = this.config.language.id;
          this.builderID = this.config.builder.id;

          this.form = new FormGroup({
            name: new FormControl(this.config.name, Validators.required),
            version: new FormControl(this.config.version, Validators.required),
            description: new FormControl(this.config.description, Validators.required),
            dockerFile: new FormControl(this.config.dockerFile, Validators.required),
            language: new FormControl(this.config.language.id, Validators.required),
            builder: new FormControl(this.config.builder.id, Validators.required)
          });

        }, (error) => {
          this.router.navigate(['/configs']);
        });
    });
  }

  onSubmit(): void {
    const configIn = new ConfigIn(
      this.form.value.name,
      this.form.value.version,
      this.form.value.description,
      this.form.value.dockerFile,
      +this.form.value.language,
      +this.form.value.builder
    );
    this.configService.update(this.config.id, configIn).subscribe((r) => this.router.navigate(['/configs']));
  }

  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    if (field !== undefined && field !== null && !control.valid) {
      return true;
    }
    return false;
  }

}
