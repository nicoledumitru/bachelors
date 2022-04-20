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
  registerData: RegisterModel = {} as RegisterModel;
  formLogin: FormGroup = {} as FormGroup;
  formRegister: FormGroup = {} as FormGroup;
  isSuccess: boolean = false;
  roles: string[] = [];
  isLoggedIn = false;

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
      username: ['', [Validators.required, Validators.minLength(6)]],
      password: ['', [Validators.required]],
    });

    this.formRegister = this.formBuilder.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required,  Validators.pattern("^([a-zA-Z0-9+-_.])+@([a-zA-Z0-9-.]+)\\.[a-z.]{2,24}$")]],
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]]
    }
    //, {
    //  validator: MustMatch('password','confirmPassword')
    
  //}
  );
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
    this.loginData.username = this.formLogin.controls['username'].value;
    this.loginData.password = this.formLogin.controls['password'].value;
    console.log(JSON.stringify(this.loginData))
    this.formLogin.controls['username'].setValue('');
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
      // let addedUser: RegisterModel = {} as RegisterModel;
      // // this.registerData
      this.registerData.username = this.formRegister.controls['username'].value;
      this.registerData.email = this.formRegister.controls['email'].value;
      this.registerData.password = this.formRegister.controls['password'].value;
      let auxPassword = this.formRegister.controls['confirmPassword'].value;
      if (auxPassword == this.registerData.password) {
        console.log(JSON.stringify(this.registerData));
        this.formRegister.controls['username'].setValue('');
        this.formRegister.controls['email'].setValue('');
        this.formRegister.controls['password'].setValue('');
        this.formRegister.controls['confirmPassword'].setValue('')
        this.loginDataService.register(this.registerData);
        this.isSuccess=true;
      } else {
        console.log("The form is not valid")
      }
      // let addedUser: RegisterModel = {} as RegisterModel;
      // addedUser.username = this.formRegister.controls['username'].value;
      // addedUser.email = this.formRegister.controls['email'].value;
      // addedUser.password = this.formRegister.controls['password'].value;
      // let auxPassword = this.formRegister.controls['confirmPassword'].value;
      // if (auxPassword == addedUser.password) {
      //   console.log(JSON.stringify(addedUser));
      //   this.formRegister.controls['username'].setValue('');
      //   this.formRegister.controls['email'].setValue('');
      //   this.formRegister.controls['password'].setValue('');
      //   this.formRegister.controls['confirmPassword'].setValue('')
      //   this.loginDataService.register(this.registerData);
      //   this.isSuccess=true;
      // } else {
      //   console.log("The form is not valid")
      // }
    } else {
      console.log("The form is not valid")
      return;
    }

  }
  
}

function MustMatch(controlName: string, matchingControlName: string) {
  return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors['mustMatch']) {
          // return if another validator has already found an error on the matchingControl
          return;
      }

      // set error on matchingControl if validation fails
      if (control.value !== matchingControl.value) {
          matchingControl.setErrors({ mustMatch: true });
      } else {
          matchingControl.setErrors(null);
      }
  }
}
