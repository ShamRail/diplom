import { Component, OnInit } from '@angular/core';
import { ConfigOut, RESTConfigService } from '../services/RESTConfigService';

@Component({
  selector: 'app-configs',
  templateUrl: './configs.component.html',
  styleUrls: ['./configs.component.css']
})
export class ConfigsComponent implements OnInit {

  configs: ConfigOut[] = [];

  constructor(
    private configService: RESTConfigService
  ) { }

  ngOnInit(): void {
   this.configService.findAll().subscribe((response) => {
     this.configs = response;
   })
  }

  remove(id: number) {
    this.configService.delete(id)
      .subscribe((r) => this.configs = this.configs.filter(c => c.id != id));

  }

}
