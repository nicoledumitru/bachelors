import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import axios from 'axios';
import { Observable } from 'rxjs';
import { Order } from '../models/order';
import { TokenStorageServiceService } from './token-storage-service.service';


const baseUrl = 'http://localhost:8080/orders';

@Injectable({
  providedIn: 'root'
})

export class OrderService {
  constructor(private http: HttpClient,
    private tokenService: TokenStorageServiceService
    ) { }

  getAll(): Observable<Order[]> {
    return this.http.get<Order[]>(baseUrl);
  }

  getNoOfBuyers(): Observable<any>{
    return this.http.get(`${baseUrl}/buyers`);
  }

  placeNewOrder() {
    let config ={ headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer '+this.tokenService.getToken()! ,
      'Access-Control-Allow-Origin': '*'
      }
    }
    axios.post(`${baseUrl}/new`,{} ,config)
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
  }
}
