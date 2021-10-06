import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Language, RESTLanguageService } from '../../services/RESTLanguageService';
import { BuilderIn, BuilderOut, RESTBuilderService } from '../../services/RESTBuilderService';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-builder-edit-form',
  templateUrl: './builder-edit-form.component.html',
  styleUrls: ['./builder-edit-form.component.css']
})
export class BuilderEditFormComponent implements OnInit {

  form: any;
  builder: BuilderOut = new BuilderOut();
  languages: Language[] = [];
  safeUrl: SafeUrl = '#';
  uploadFile: any;

  constructor(
    private languageCrudService: RESTLanguageService,
    private builderService: RESTBuilderService,
    private activatedRoute: ActivatedRoute,
    private route: Router,
    private domSanitizer: DomSanitizer
  ) {
    this.form = new FormGroup({
      name: new FormControl('', Validators.required),
      version: new FormControl('', Validators.required),
      languages: new FormControl('', Validators.required),
      file: new FormControl('')
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
              languages: new FormControl(this.builder.languages.map(l => l.id), Validators.required),
              file: new FormControl('')
            });
            this.safeUrl = this.transformUrl(this.builder.logo);
            URL.revokeObjectURL(this.safeUrl.toString());
          }, (error) => {
            this.route.navigate(['/builders']);
          });
    });
  }

  onSubmit(): void {

    const formData = new FormData();
    formData.append('name', this.form.value.name);
    formData.append('version', this.form.value.version);
    formData.append('languageID', this.form.value.languages);
    formData.append('file', this.uploadFile ? this.uploadFile : undefined);

    this.builderService.updateByFormData(this.builder.id, formData).subscribe((resp) => {
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

  onFileValueChanged($event: any): void {
    if ($event.target.files.length > 0) {
      this.uploadFile = $event.target.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        const url = reader.result as string;
        this.safeUrl = this.domSanitizer.bypassSecurityTrustUrl(url);
        URL.revokeObjectURL(this.safeUrl.toString());
      };
      reader.readAsDataURL(this.uploadFile);
    }
  }

  transformUrl(logo: string): SafeUrl {
    const url = 'data:image/jpeg;charset=utf-8;base64,' + logo;
    return this.domSanitizer.bypassSecurityTrustUrl(url);
  }

}
