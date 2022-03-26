import { Injectable } from '@angular/core';

const TOKEN_KEY = 'auth-token';
const REFRESHTOKEN_KEY = 'auth-refreshtoken';
const USER_KEY = 'auth-user'

@Injectable({
  providedIn: 'root'
})
export class TokenStorageServiceService {

  constructor() { }

  signOut() : void{
    window.sessionStorage.clear();
  }

  public saveToken(token : string) : void{
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY,token);

    const answerLogin = this.getAnswerLogin();
    if ( answerLogin.id ){
      this.saveAnswerLogin({...answerLogin,accessToken : token});
    }
  }

  public getToken(): string | null {
    return window.sessionStorage.getItem(TOKEN_KEY);
  }

  public saveRefreshToken(token:string): void{
    window.sessionStorage.removeItem(REFRESHTOKEN_KEY);
    window.sessionStorage.setItem(REFRESHTOKEN_KEY,token);
  }

  public getRefreshToken(token: string){
    return window.sessionStorage.getItem(REFRESHTOKEN_KEY);
  }

  public saveAnswerLogin(answerLogin : any) : void{
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(answerLogin));
  }

  public getAnswerLogin(): any {
    const answerLogin = window.sessionStorage.getItem(USER_KEY);
    if (answerLogin) {
      return JSON.parse(answerLogin);
    }

    return {};
  }

}

