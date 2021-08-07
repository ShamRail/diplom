import {ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot} from "@angular/router";
import {App, AppService} from "../services/AppService";
import {Observable, of} from "rxjs";
import {Injectable} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AppResolver implements Resolve<App> {

  constructor(private appService: AppService, private router: Router) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<App> | Promise<App> | App {
    return this.appService.run(route.params.projectID)
      .pipe(
        map(res => res),
        catchError((err: HttpErrorResponse, caught: Observable<any>) => {
          if (err.status === 404) {
            this.router.navigateByUrl('/error/404');
          }
          if (err.status === 409) {
            this.router.navigateByUrl('/error/409');
          }
          return of();
        })
      )
  }

}
