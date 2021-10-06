import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Language } from './RESTLanguageService';
import {environment} from '../../environments/environment';

export class BuilderIn {

  name = '';
  version = '';
  languageID: number[] = [];

  constructor(name: string, version: string, languageID: number[]) {
    this.name = name;
    this.version = version;
    this.languageID = languageID;
  }

}

export class BuilderOut {

  id = 0;
  name = '';
  version = '';
  languages: Language[] = [];
  logo = '';

}

@Injectable({
  providedIn: 'root'
})
export class RESTBuilderService {

  constructor(private httpClient: HttpClient) {}

  save(builder: BuilderIn): Observable<BuilderOut> {
    return this.httpClient.post<BuilderOut>(
      `${environment.api}/builders`, builder
    );
  }

  saveByFormData(builder: FormData): Observable<BuilderOut> {
    return this.httpClient.post<BuilderOut>(
      `${environment.api}/builders`, builder
    );
  }

  update(id: number, builder: BuilderIn): Observable<void> {
    return this.httpClient.put<void>(
      `${environment.api}/builders/${id}`, builder
    );
  }

  updateByFormData(id: number, formData: FormData): Observable<void> {
    return this.httpClient.put<void>(
      `${environment.api}/builders/${id}`, formData
    );
  }

  delete(id: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${environment.api}/builders/${id}`
    );
  }

  findById(id: number): Observable<BuilderOut> {
    return this.httpClient.get<BuilderOut>(
      `${environment.api}/builders/${id}`
    );
  }

  findAll(): Observable<BuilderOut[]> {
    return this.httpClient.get<BuilderOut[]>(
      `${environment.api}/builders`
    );
  }
}
