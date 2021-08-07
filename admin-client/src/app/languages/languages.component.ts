import { Component, OnInit } from '@angular/core';
import { Language, RESTLanguageService } from '../services/RESTLanguageService';

@Component({
  selector: 'app-languages',
  templateUrl: './languages.component.html',
  styleUrls: ['./languages.component.css']
})
export class LanguagesComponent implements OnInit {

  languages: Language[] = [];

  constructor(private languageCrudService: RESTLanguageService) { }

  ngOnInit(): void {
    this.languageCrudService.findAll().subscribe((resp) => {
        for (let l of resp) {
          this.languages.push(new Language(
            l.name, l.version, l.id
          ));
        }
    });
  }

  findAll(): Language[] {
    return this.languages;
  }

  remove(id: number): void {
      this.languageCrudService.delete(id)
        .subscribe((res) => {
          this.languages = this.languages.filter((l) => l.id != id);
        }, error => alert('Удаление невозможно, т.к. есть зависимые сущности. Для удаления сначала удалите их'));
  }

}
