import {Component, Input, OnInit} from '@angular/core';
import {App} from "../services/AppService";
import {Config, ConfigService} from "../services/ConfigService";

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
export class InfoComponent implements OnInit {

  @Input() app: App = new App();
  config: Config = new Config();

  constructor(private configService: ConfigService) { }

  ngOnInit(): void {
    this.configService.findById(this.app.project.configurationId)
      .subscribe(
        config => this.config = config,
        error => console.log(error)
      );
  }

  updateApp(event: App) {
    this.app = event;
  }

}
