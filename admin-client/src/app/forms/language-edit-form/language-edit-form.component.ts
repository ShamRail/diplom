import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Language, RESTLanguageService} from '../../services/RESTLanguageService';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-language-edit-form',
  templateUrl: './language-edit-form.component.html',
  styleUrls: ['./language-edit-form.component.css']
})
export class LanguageEditFormComponent implements OnInit {

  language: any = {name: 'stub', version: 'stub'};
  form: FormGroup;
  safeUrl: SafeUrl = '#';

  constructor(
    private languageCrudService: RESTLanguageService,
    private activatedRouter: ActivatedRoute,
    private router: Router,
    private domSanitizer: DomSanitizer
  ) {
    this.form = new FormGroup({
      name: new FormControl('', Validators.required),
      version: new FormControl('', Validators.required),
      file: new FormControl(Validators.required)
    });
  }

  ngOnInit(): void {
    this.activatedRouter.params
      .subscribe((params: Params) => {
        this.languageCrudService.findById(+params.id)
          .subscribe((resp) => {
            this.language = resp;
            this.form = new FormGroup({
              name: new FormControl(this.language.name, Validators.required),
              version: new FormControl(this.language.version, Validators.required),
              file: new FormControl('', Validators.required)
            });
            this.safeUrl = this.transformUrl(this.language.logo);
            URL.revokeObjectURL(this.safeUrl.toString());
          });
      });
  }

  onSubmit(): void {

    const formData = new FormData();
    formData.append('name', this.form.value.name);
    formData.append('version', this.form.value.version);
    formData.append('file', this.form.value.file);

    this.languageCrudService.updateFormData(this.language.id, formData)
      .subscribe(() => {
        this.router.navigate(['/languages']);
    });

  }

  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    return control !== undefined && control !== null && !control.valid;
  }

  transformUrl(logo: string): SafeUrl {
    const url = 'data:image/jpeg;charset=utf-8;base64,' + logo;
    return this.domSanitizer.bypassSecurityTrustUrl(url);
  }


  onFileValueChanged(event: any): any {
    if (event.target.files.length > 0) {
      const targetFile = event.target.files[0];
      this.form.patchValue({
        file: targetFile
      });
      const reader = new FileReader();
      reader.onload = () => {
        const url = reader.result as string;
        this.safeUrl = this.domSanitizer.bypassSecurityTrustUrl(url);
        URL.revokeObjectURL(this.safeUrl.toString());
      };
      reader.readAsDataURL(targetFile);
    }
  }

}
