import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginData } from '../models/login-data';
import { RegisterModel } from '../models/register-data';
import { LoginDataService } from '../services/login-data.service';
import { TokenStorageServiceService } from '../services/token-storage-service.service';

@Component({
  selector: 'app-login-register',
  templateUrl: './login-register.component.html',
  styleUrls: ['./login-register.component.scss']
})
export class LoginRegisterComponent implements OnInit {

  loginData: LoginData = {} as LoginData;
  formLogin: FormGroup = {} as FormGroup;
  formRegister: FormGroup = {} as FormGroup;
  isSuccess: boolean = false;

  @Output() getLoginData = new EventEmitter<string>();

  constructor(
    private loginDataService: LoginDataService,
    private formBuilder: FormBuilder,
    private tokenService: TokenStorageServiceService,
    private router: Router) {
  }

  initForm() {
  }

  ngOnInit(): void {

    this.formLogin = this.formBuilder.group({
      email: ['', [Validators.required, Validators.pattern("^([a-zA-Z0-9+-_.])+@([a-zA-Z0-9-.]+)\\.[a-z.]{2,24}$")]],
      password: ['', [Validators.required]],
    });

    this.formRegister = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(6)]],
      email: ['', [Validators.required,  Validators.pattern("^([a-zA-Z0-9+-_.])+@([a-zA-Z0-9-.]+)\\.[a-z.]{2,24}$")]],
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]]
    }, {
      validator: MustMatch('password', 'confirmPassword')
    });
  }

  registerAcitvate() {
    const container = document.getElementById('container');
    const signUpButton = document.getElementById('signUp');
    container?.classList.add("right-panel-active");
    signUpButton?.addEventListener('click', () => {
      container?.classList.add("right-panel-active");
    })
  }

  loginActivate() {
    const container = document.getElementById('container');
    const signInButton = document.getElementById('signIn');
    container?.classList.remove("right-panel-active");
    signInButton?.addEventListener('click', () => {
      container?.classList.remove("right-panel-active");
    });
  }

  saveLoginData() {
    this.loginData.email = this.formLogin.controls['email'].value;
    this.loginData.password = this.formLogin.controls['password'].value;
    console.log(JSON.stringify(this.loginData))
    this.formLogin.controls['email'].setValue('');
    this.formLogin.controls['password'].setValue('');
    let auxAnswearLogin = this.loginDataService.authentificate(this.loginData);
    let auxToken;
    auxAnswearLogin.subscribe(res => {
      this.tokenService.saveToken(res.token);
      this.tokenService.saveRefreshToken(res.refreshToken);
      this.tokenService.saveAnswerLogin(res);
      if (this.tokenService.getToken() !== "") {
        this.router.navigateByUrl('/');
      }
    })
  }

  saveRegisterData() {
    if (this.formRegister.valid) {
      let addedUser: RegisterModel = {} as RegisterModel;
      addedUser.displayName = this.formRegister.controls['name'].value;
      addedUser.email = this.formRegister.controls['email'].value;
      addedUser.password = this.formRegister.controls['password'].value;
      let auxPassword = this.formRegister.controls['confirmPassword'].value;
      if (auxPassword == addedUser.password) {
        console.log(JSON.stringify(addedUser));
        this.formRegister.controls['name'].setValue('');
        this.formRegister.controls['email'].setValue('');
        this.formRegister.controls['password'].setValue('');
        this.formRegister.controls['confirmPassword'].setValue('')
        this.loginDataService.register(addedUser);
        this.isSuccess=true;
      } else {
        console.log("The form is not valid")
      }
    } else {
      console.log("The form is not valid")
      return;
    }

  }

}
function MustMatch(arg0: string, arg1: string) {
  throw new Error('Function not implemented.');
}

