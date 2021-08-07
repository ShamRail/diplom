import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {TerminalComponent} from './terminal/terminal.component';
import {PanelComponent} from './panel/panel.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MainComponent} from './main/main.component';
import { InfoComponent } from './info/info.component';
import { TimePipe } from './pipes/time.pipe';
import { NotFoundComponent } from './errors/not-found/not-found.component';
import { ProjectNotFoundComponent } from './errors/project-not-found/project-not-found.component';
import { BufferComponent } from './buffer/buffer.component';

@NgModule({
  declarations: [
    AppComponent,
    TerminalComponent,
    PanelComponent,
    MainComponent,
    InfoComponent,
    TimePipe,
    NotFoundComponent,
    ProjectNotFoundComponent,
    BufferComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule, ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
