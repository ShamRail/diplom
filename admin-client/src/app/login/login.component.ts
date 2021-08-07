import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthService, User} from '../services/AuthService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: FormGroup = new FormGroup({});
  authFall = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      login: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });
  }

  onSubmit(): void {
    const user = new User(
      this.form.value.login,
      this.form.value.password
    );
    this.authService.login(user)
      .subscribe(
        (response) => {
          this.authFall = false;
          this.router.navigate(['/languages']);
        },
        error => {
          console.log(error);
          this.authFall = true;
        }
        );
  };

  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    if (control !== undefined && control !== null && !control.valid) {
      return true;
    }
    return false;
  }
}
