import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Language, RESTLanguageService} from '../../services/RESTLanguageService';
import {BuilderOut, RESTBuilderService} from '../../services/RESTBuilderService';
import {ConfigIn, RESTConfigService} from '../../services/RESTConfigService';

@Component({
  selector: 'app-config-create-form',
  templateUrl: './config-create-form.component.html',
  styleUrls: ['./config-create-form.component.css']
})
export class ConfigCreateFormComponent implements OnInit {

  form: any;
  languages: Language[] = [];
  builders: BuilderOut[] = [];

  constructor(
    private languageService: RESTLanguageService,
    private builderService: RESTBuilderService,
    private configService: RESTConfigService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      name: new FormControl('', Validators.required),
      version: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      dockerFile: new FormControl('', Validators.required),
      language: new FormControl('', Validators.required),
      builder: new FormControl('', Validators.required)
    });
    this.languageService.findAll().subscribe((r) => this.languages = r);
    this.builderService.findAll().subscribe((r) => this.builders = r);
  }

  onSubmit() {
    this.configService.save(new ConfigIn(
      this.form.value.name,
      this.form.value.version,
      this.form.value.description,
      this.form.value.dockerFile,
      +this.form.value.language,
      +this.form.value.builder
    )).subscribe((resp) => {
      this.router.navigate(['/configs']);
    });
  }
  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    if (field !== undefined && field !== null && !control.valid) {
      return true;
    }
    return false;
  }
}
