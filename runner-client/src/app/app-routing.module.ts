import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from "./main/main.component";
import {AppResolver} from "./resolvers/AppResolver";
import {NotFoundComponent} from "./errors/not-found/not-found.component";
import {ProjectNotFoundComponent} from "./errors/project-not-found/project-not-found.component";
import {NotBuiltComponent} from "./errors/not-built/not-built.component";

const routes: Routes = [
  {
    path: 'app/:projectID',
    component: MainComponent,
    resolve: {
      app: AppResolver
    }
  },
  {path: 'error/404', component: ProjectNotFoundComponent},
  {path: 'error/409', component: NotBuiltComponent},
  {path: '**', component: NotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
