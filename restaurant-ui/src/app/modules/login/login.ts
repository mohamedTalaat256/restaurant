import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Button } from "primeng/button";
import { FormInput } from "../../shared/components/form-input/form-input";
import { LoginService } from './login.service';
import { TranslateService } from '../../core/service/translate.service';
import { env } from '../../../environment/env';
import { ApplicationSetting } from '../../core/model/application-setting.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.scss'],

  providers: [],
  imports: [Button, FormInput, ReactiveFormsModule, FormsModule]
})
export class Login {

  authenticated = false;

  loginForm: FormGroup;
  readonly loginService = inject(LoginService);
  readonly translate = inject(TranslateService);

   applicationSettings: ApplicationSetting | null = null;
  imageUrl = env.baseUrl;



  constructor(private fb: FormBuilder) {
    this.applicationSettings = JSON.parse(localStorage.getItem('applicationSettings') || '{}');

    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.maxLength(30), Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    });
  }
  onLogin() {
    if (!this.loginForm.valid) {
      this.loginForm.markAllAsTouched();
      return;
    } else {
      this.loginService.login(this.loginForm.value);
    }
  }
}
