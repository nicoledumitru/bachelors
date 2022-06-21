import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import axios from 'axios';
import { Observable } from 'rxjs';
import { Review } from '../models/review-data';
import { TokenStorageServiceService } from './token-storage-service.service';

const baseUrl = 'http://localhost:8080/reviews';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor(private http: HttpClient, private tokenService: TokenStorageServiceService) { }

  getAll(pid: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${baseUrl}/${pid}`);
  }

  addReview(data: any) {
    let config ={ headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer '+this.tokenService.getToken()! ,
      'Access-Control-Allow-Origin': '*'
      }
    }
    axios.post(`${baseUrl}/add`,data ,config)
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
  }
}
