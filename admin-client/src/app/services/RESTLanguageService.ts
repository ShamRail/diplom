import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../environments/environment';

export class Language {

  id: any = 0;
  name = '';
  version = '';
  logo = '';

  constructor(name: string, version: string, id?: number) {
    this.id = id;
    this.name = name;
    this.version = version;
  }

}

@Injectable({
  providedIn: 'root'
})
export class RESTLanguageService {

  constructor(private httpClient: HttpClient) {
  }

  save(language: Language): Observable<Language> {
    return this.httpClient.post<Language>(
      `${environment.api}/languages`, language
    );
  }

  saveFormData(data: FormData): Observable<Language> {
    return this.httpClient.post<Language>(
      `${environment.api}/languages`, data
    );
  }

  update(id: number, language: Language): Observable<void> {
    return this.httpClient.put<void>(
      `${environment.api}/languages/${id}`, language
    );
  }

  updateFormData(id: number, data: FormData): Observable<void> {
    return this.httpClient.put<void>(
      `${environment.api}/languages/${id}`, data
    );
  }

  delete(id: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${environment.api}/languages/${id}`
    );
  }

  findById(id: number): Observable<Language> {
    return this.httpClient.get<Language>(
      `${environment.api}/languages/${id}`
    );
  }

  findAll(): Observable<Language[]> {
    return this.httpClient.get<any>(
      `${environment.api}/languages`
    );
  }

}
