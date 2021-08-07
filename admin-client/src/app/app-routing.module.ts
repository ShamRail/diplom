import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LanguagesComponent } from './languages/languages.component';
import { BuildersComponent } from './builders/builders.component';
import { ConfigsComponent } from './configs/configs.component';
import { LoginComponent } from './login/login.component';
import { LanguageEditFormComponent } from './forms/language-edit-form/language-edit-form.component';
import { BuilderEditFormComponent } from './forms/builder-edit-form/builder-edit-form.component';
import { ConfigComponent } from './config/config.component';
import { ConfigCreateFormComponent } from './forms/config-create-form/config-create-form.component';
import {NotFoundComponent} from './errors/not-found/not-found.component';

const routes: Routes = [
  {path: '', redirectTo: '/languages', pathMatch: 'full'},
  {path: 'languages/:id', component: LanguageEditFormComponent},
  {path: 'languages', component: LanguagesComponent},
  {path: 'builders/:id', component: BuilderEditFormComponent},
  {path: 'builders', component: BuildersComponent},
  {path: 'configs/create', component: ConfigCreateFormComponent},
  {path: 'configs/:id', component: ConfigComponent},
  {path: 'configs', component: ConfigsComponent},
  {path: 'login', component: LoginComponent},
  {path: '**', component: NotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
