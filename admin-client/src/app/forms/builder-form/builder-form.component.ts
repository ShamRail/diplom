import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { Language, RESTLanguageService } from '../../services/RESTLanguageService';
import { BuilderIn, BuilderOut, RESTBuilderService } from '../../services/RESTBuilderService';

@Component({
  selector: 'app-builder-form',
  templateUrl: './builder-form.component.html',
  styleUrls: ['./builder-form.component.css']
})
export class BuilderFormComponent implements OnInit {

  form: any;
  languages: Language[] = [];
  selectSize = 1;

  @Output() onAdd: EventEmitter<BuilderOut> = new EventEmitter<BuilderOut>();

  constructor(
    private languageCrudService: RESTLanguageService,
    private builderService: RESTBuilderService) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      name: new FormControl('', Validators.required),
      version: new FormControl('', Validators.required),
      languages: new FormControl('', Validators.required)
    });
    this.languageCrudService.findAll()
      .subscribe((langs) => {
        this.languages = langs;
      });
  }
  onSubmit(): void {
    const builder = new BuilderIn(
      this.form.value.name, this.form.value.version, this.form.value.languages
    );
    this.builderService.save(builder)
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

}
