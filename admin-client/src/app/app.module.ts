import {NgModule, Provider} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LanguagesComponent } from './languages/languages.component';
import { BuildersComponent } from './builders/builders.component';
import { ConfigsComponent } from './configs/configs.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { LanguageFormComponent } from './forms/language-form/language-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LanguageEditFormComponent } from './forms/language-edit-form/language-edit-form.component';
import { BuilderFormComponent } from './forms/builder-form/builder-form.component';
import { BuilderEditFormComponent } from './forms/builder-edit-form/builder-edit-form.component';
import { ConfigComponent } from './config/config.component';
import { ConfigCreateFormComponent } from './forms/config-create-form/config-create-form.component';

import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { NotFoundComponent } from './errors/not-found/not-found.component';
import {AuthInterceptor} from './interceptors/AuthInterceptor';

const INTERCEPTOR_PROVIDER: Provider = {
  provide: HTTP_INTERCEPTORS,
  useClass: AuthInterceptor,
  multi: true
};

@NgModule({
  declarations: [
    AppComponent,
    LanguagesComponent,
    BuildersComponent,
    ConfigsComponent,
    HomeComponent,
    LoginComponent,
    LanguageFormComponent,
    LanguageEditFormComponent,
    BuilderFormComponent,
    BuilderEditFormComponent,
    ConfigComponent,
    ConfigCreateFormComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule, ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [INTERCEPTOR_PROVIDER],
  bootstrap: [AppComponent]
})
export class AppModule { }
