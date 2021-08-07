import { Injectable } from '@angular/core';
import { InMemoryLanguageCrudService, Language } from './inMemoryLanguageCrudService';
import { Builder, InMemoryBuilderService } from './InMemoryBuilderService';

export interface Config {
  id?: number,
  name: string,
  version: string,
  description: string,
  dockerContent: string,
  language: any,
  builder: any
}

@Injectable({
  providedIn: 'root'
})
export class InMemoryConfigService {

  private configs: Config[] = [];

  private ids: number = 1;

  constructor(
    private languageService: InMemoryLanguageCrudService,
    private builderService: InMemoryBuilderService
  ) {
    this.save({
      name: 'Java + Maven', version: '1.0', description: 'Some desc', dockerContent: '',
      language: languageService.findById(1),
      builder: builderService.findById(1)
    });
    this.save({
      name: 'Python + pip', version: '1.0', description: 'Some desc', dockerContent: '',
      language: languageService.findById(2),
      builder: builderService.findById(2)
    });
    this.save({
      name: 'JS + npm', version: '1.0', description: 'Some desc', dockerContent: '',
      language: languageService.findById(3),
      builder: builderService.findById(3)
    });
    this.save({
      name: 'TS + npm', version: '1.0', description: 'Some desc', dockerContent: '',
      language: languageService.findById(4),
      builder: builderService.findById(4)
    });
    this.save({
      name: 'C++ & CMake', version: '1.0', description: 'Some desc', dockerContent: '',
      language: languageService.findById(5),
      builder: builderService.findById(5)
    });
  }

  save(config: Config): void {
    config.id = this.ids++;
    this.configs.push(config);
  }

  updateById(id: number, config: Config): boolean {
    let index: number = this.configs.findIndex((c => {return c.id === id}));
    if (index === -1) {
      return false;
    }
    config.id = id;
    this.configs[index] = config;
    return true;
  }

  deleteById(id: number | undefined):boolean {
    let index: number = this.configs.findIndex((c => {return c.id === id}));
    if (index === -1) {
      return false;
    }
    this.configs.splice(index, 1);
    return true;
  }

  findById(id: number): Config | null {
    let index: number = this.configs.findIndex((c => {return c.id === id}));
    if (index === -1) {
      return null;
    }
    return this.configs[index];
  }

  findAll(): Config[] {
    return this.configs;
  }

}
