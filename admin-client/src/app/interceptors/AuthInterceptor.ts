import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {AuthService} from '../services/AuthService';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor{

  constructor(private authService: AuthService, private router: Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.authService.isAuth()) {
      next.handle(req);
    }
    if (req.url.includes('/login')) {
      next.handle(req);
    }
    if (!this.authService.isAuth()) {
      this.router.navigate(['/login']);
    }
    const cloned = req.clone({
      headers: req.headers.append('Authorization', `Bearer ${this.authService.getToken()}`)
    });
    return next.handle(cloned);
  }

}
