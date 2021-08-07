import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {App} from "../services/AppService";
import {ActivatedRoute} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  app: App = new App();

  constructor(
    private activeRoute: ActivatedRoute
  ) {

  }

  ngOnInit(): void {
    this.activeRoute.data.subscribe(
      (data) => {
        console.log(data.app);
        this.app = data.app;
        this.app.isActive = true;
        },
      (error: HttpErrorResponse) => {
        console.log(error.status);
      }
    );
  }

}
