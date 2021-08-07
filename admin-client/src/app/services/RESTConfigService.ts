import { Injectable } from '@angular/core';
import { Language } from './RESTLanguageService';
import { BuilderOut } from './RESTBuilderService';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import {environment} from '../../environments/environment';

export class ConfigIn {

  name = '';
  version = '';
  description = '';
  dockerContent = '';
  languageID = 0;
  builderID = 0;

  constructor(name: string,
              version: string,
              description: string,
              dockerContent: string,
              languageID: number,
              builderID: number) {
    this.name = name;
    this.version = version;
    this.description = description;
    this.dockerContent = dockerContent;
    this.languageID = languageID;
    this.builderID = builderID;
  }

}

export class ConfigOut {

  id = 0;
  name = '';
  version = '';
  description = '';
  dockerFile = '';
  language: Language = new Language('stub', 'stub');
  builder: BuilderOut = new BuilderOut();

}

@Injectable({
  providedIn: 'root'
})
export class RESTConfigService {

  constructor(private httpClient: HttpClient) {}

  save(config: ConfigIn): Observable<ConfigOut> {
    return this.httpClient.post<ConfigOut>(`${environment.api}/configuration`, config);
  }

  update(id: number, config: ConfigIn): Observable<void> {
    return this.httpClient.put<void>(`${environment.api}/configuration/${id}`, config);
  }

  delete(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${environment.api}/configuration/${id}`);
  }

  findById(id: number): Observable<ConfigOut> {
    return this.httpClient.get<ConfigOut>(`${environment.api}/configuration/${id}`);
  }

  findAll(): Observable<ConfigOut[]> {
    return this.httpClient.get<ConfigOut[]>(`${environment.api}/configuration/all`);
  }

}


