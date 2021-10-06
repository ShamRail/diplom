import { Component, OnInit } from '@angular/core';
import { BuilderOut, RESTBuilderService } from '../services/RESTBuilderService';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-builders',
  templateUrl: './builders.component.html',
  styleUrls: ['./builders.component.css']
})
export class BuildersComponent implements OnInit {

  builders: BuilderOut[] = [];
  safeUrls: SafeUrl[] = [];

  constructor(
    private builderService: RESTBuilderService,
    private domSanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.builderService.findAll().subscribe((resp) => {
      this.builders = resp;
      for (const builder of this.builders) {
        this.safeUrls.push(this.transformUrl(builder.logo));
      }
      this.safeUrls.forEach(url => URL.revokeObjectURL(url.toString()));
    });
  }

  remove(id: number): void {
    this.builderService.delete(id)
      .subscribe((resp) => {
        this.builders = this.builders.filter(b => b.id !== id);
      }, error => alert('Удаление невозможно, т.к. есть зависимые сущности. Для удаления сначала удалите их'));
  }

  addBuilder(builder: BuilderOut): void {
    this.builders.push(builder);
  }

  transformUrl(logo: string): SafeUrl {
    const url = 'data:image/jpeg;charset=utf-8;base64,' + logo;
    return this.domSanitizer.bypassSecurityTrustUrl(url);
  }

}
