import { Injectable } from '@angular/core';
import { InMemoryLanguageCrudService, Language } from './inMemoryLanguageCrudService';

export interface Builder {
  id?: number,
  name: string,
  version: string
  language: any
}

@Injectable({
  providedIn: 'root'
})
export class InMemoryBuilderService {

  private builders: Builder[] = [];

  private ids: number = 1;

  constructor(languageService: InMemoryLanguageCrudService) {
    this.save({name: 'Maven', version: '3.6', language: languageService.findById(1)})
    this.save({name: 'pip', version: '6.0', language: languageService.findById(2)})
    this.save({name: 'npm', version: '14.0', language: languageService.findById(3)})
    this.save({name: 'npm', version: '15', language: languageService.findById(4)})
    this.save({name: 'CMake', version: '5.0', language: languageService.findById(5)})
  }

  save(builder: Builder): void {
    builder.id = this.ids++;
    this.builders.push(builder);
  }

  updateById(id: number, builder: Builder): boolean {
    let index: number = this.builders.findIndex((b => {
      return b.id === id
    }));
    if (index == -1) {
      return false;
    }
    builder.id = id;
    this.builders[index] = builder;
    return true;
  }

  deleteById(id: number | undefined): boolean {
    let index: number = this.builders.findIndex((b => {
      return b.id === id
    }));
    if (index == -1) {
      return false;
    }
    this.builders.splice(index, 1);
    return true;
  }

  findAll(): Builder[] {
    return this.builders;
  }

  findById(id: number): Builder | null {
    let index: number = this.builders.findIndex((b => {
      return b.id === id
    }));
    return index === -1 ? null : this.builders[index];
  }

}
