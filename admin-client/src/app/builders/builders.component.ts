import { Component, OnInit } from '@angular/core';
import { BuilderOut, RESTBuilderService } from '../services/RESTBuilderService';

@Component({
  selector: 'app-builders',
  templateUrl: './builders.component.html',
  styleUrls: ['./builders.component.css']
})
export class BuildersComponent implements OnInit {

  builders: BuilderOut[] = [];

  constructor(private builderService: RESTBuilderService) { }

  ngOnInit(): void {
    this.builderService.findAll().subscribe((resp) => {
      console.log(resp);
      this.builders = resp;
    })
  }

  remove(id: number): void {
    this.builderService.delete(id)
      .subscribe((resp) => {
        this.builders = this.builders.filter(b => b.id != id);
      }, error => alert('Удаление невозможно, т.к. есть зависимые сущности. Для удаления сначала удалите их'));
  }

  addBuilder(builder: BuilderOut) {
    this.builders.push(builder);
  }
}
