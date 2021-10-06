import { Component, OnInit } from '@angular/core';
import { Language, RESTLanguageService } from '../services/RESTLanguageService';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-languages',
  templateUrl: './languages.component.html',
  styleUrls: ['./languages.component.css']
})
export class LanguagesComponent implements OnInit {

  languages: Language[] = [];
  logoUrls: SafeUrl[] = [];

  constructor(private languageCrudService: RESTLanguageService, private domSanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.languageCrudService.findAll().subscribe((resp) => {
        this.languages = resp;
        for (const language of this.languages) {
          this.logoUrls.push(this.transformUrl(language.logo));
        }
        this.logoUrls.forEach(url => URL.revokeObjectURL(url.toString()));
    });
  }

  findAll(): Language[] {
    return this.languages;
  }

  remove(id: number): void {
      this.languageCrudService.delete(id)
        .subscribe((res) => {
          this.languages = this.languages.filter((l) => l.id !== id);
        }, error => alert('Удаление невозможно, т.к. есть зависимые сущности. Для удаления сначала удалите их'));
  }

  transformUrl(logo: string): SafeUrl {
    const url = 'data:image/jpeg;charset=utf-8;base64,' + logo;
    return this.domSanitizer.bypassSecurityTrustUrl(url);
  }

  onSave($event: Language): void {
    this.languages.push($event);
    const transformUrl = this.transformUrl($event.logo);
    this.logoUrls.push(transformUrl);
    URL.revokeObjectURL(transformUrl.toString());
  }
}
