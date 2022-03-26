import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AnswerLogin } from '../models/answerLogin';
import { HttpClient } from '@angular/common/http';
import { LoginData } from '../models/login-data';
import { RegisterModel } from '../models/register-data';

@Injectable({
  providedIn: 'root'
})
export class LoginDataService {
  private loginUrl: string;
  private signUpUrl : string;
  private loginAnswerData = {} as Observable<AnswerLogin>;

  constructor(private http: HttpClient) {
    this.loginUrl = "http://localhost:8080/login";
    this.signUpUrl = "http://localhost:8080/register";
  }

  authentificate(loginData: LoginData) : Observable<AnswerLogin>{
    this.loginAnswerData = this.http.post<AnswerLogin>(this.loginUrl,loginData) as Observable<AnswerLogin>;
    this.loginAnswerData.subscribe( (res) =>{
      console.log(res);
    })
    return this.loginAnswerData;
  }

  register(registerData: RegisterModel) : Observable<RegisterModel>{
    this.http.post<RegisterModel>(this.signUpUrl,registerData).subscribe( res =>{
      console.log(res);
    })
    return this.http.post<RegisterModel>(this.signUpUrl,registerData) as Observable<RegisterModel>;
  }
}
