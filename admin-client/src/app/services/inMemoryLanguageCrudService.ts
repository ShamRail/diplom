import { InMemoryCrudService } from './inMemoryCrud.service';
import { Injectable } from '@angular/core';

export interface Language {
  id?: number,
  name: string,
  version: string
}

@Injectable({
  providedIn: 'root'
})
export class InMemoryLanguageCrudService implements InMemoryCrudService<Language> {

  private languages: Language[] = [];

  private ids: number = 1;

  constructor() {
    this.save({name: 'Java', version: '16'});
    this.save({name: 'Python', version: '3.6'});
    this.save({name: 'JavaScript', version: 'ES6'});
    this.save({name: 'TypeScript', version: '2.7'});
    this.save({name: 'C++', version: '17'});
  }


  save(model: Language): void {
    model.id = this.ids++;
    this.languages.push(model);
  }

  deleteById(id: number | undefined): boolean {
    let index = this.languages.findIndex(value => {return value.id === id});
    if (index == -1) {
      return false;
    }
    this.languages.splice(index, 1);
    return true;
  }

  findAll(): Language[] {
    let copy: Language[] = [];
    for (let language of this.languages) {
      copy.push({
        id: language.id, name: language.name, version: language.version
      })
    }
    return copy;
  }

  findById(id: number): Language | null {
    let index = this.languages.findIndex(value => {return value.id === id});
    if (index == -1) {
      return null;
    }
    return this.languages[index];
  }

  updateById(id: number | undefined, model: Language): boolean {
    let index = this.languages.findIndex(value => {return value.id === id});
    if (index == -1) {
      return false;
    }
    model.id = id;
    this.languages[index] = model;
    return true;
  }

}
