import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Language } from '../services/inMemoryLanguageCrudService';

class Result {
  _embedded: {
    languages: Language[];
  };

  constructor() {
    this._embedded = {languages: []};
  }
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  result: Result = new Result();

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
  }

  onClick(): void {
    console.log('Sending request ...');
    this.http.get<Result>('http://localhost:8080/app-build-runner/admin/languages')
      .subscribe(resp => {
        this.result = resp;
    });
  }

}
