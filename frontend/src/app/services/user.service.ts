import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const url = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) { }

  // getPublicContent(): Observable<any> {
  //   return this.http.get(url + 'all', { responseType: 'text' });
  // }

  getUserBoard(): Observable<any> {
    return this.http.get(url, { responseType: 'text' });
  }

  // getModeratorBoard(): Observable<any> {
  //   return this.http.get(url + 'mod', { responseType: 'text' });
  // }

  getAdminBoard(): Observable<any> {
    return this.http.get(url + '/users/admin/all', { responseType: 'text' });
  }
}