import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {environment} from '../../environments/environment';

export class User {
  login = '';
  password = '';

  constructor(login: string, password: string) {
    this.login = login;
    this.password = password;
  }
}

export class Token {
  token = '';
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private auth = false;
  private token: string | null = '';

  constructor(private httpClient: HttpClient) {
  }

  isAuth(): boolean {
    return this.auth;
  }

  login(user: User): Observable<Token> {
    return this.httpClient.post<Token>(`${environment.api}/login`, user)
      .pipe(
        map(response => {
          this.auth = true;
          this.token = response.token;
          return response;
        })
      );
  }

  getToken(): any {
    return this.token;
  }

  logout(): void {
    this.auth = false;
  }
}
