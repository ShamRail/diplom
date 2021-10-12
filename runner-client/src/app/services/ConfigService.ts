import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

export class Language {

  name: string = '';
  version: string = '';

}

export class Builder {

  name: string = '';
  version: string = '';

}

export class Config {

  id: number = 0;
  name: string = '';
  description: string = '';
  language: Language = new Language();
  builder: Builder = new Builder();

}

@Injectable({
  providedIn: "root"
})
export class ConfigService {

  constructor(private httpClient: HttpClient) { }

  findById(id: number): Observable<Config> {
    return this.httpClient.get<Config>(`${environment.configApi}/configuration/${id}`);
  }

}
