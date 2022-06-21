import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { TokenStorageServiceService } from './token-storage-service.service';
import { Product } from '../models/product.model';
import axios from 'axios';

const getCartUrl = "http://localhost:8080/cart";
const postToCartUrl = "http://localhost:8080/cart/add";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(private http: HttpClient,
    private tokenService: TokenStorageServiceService) { }

  getCartItems(): Observable<any> {
    return this.http.get(getCartUrl
      // {headers: {
      // 'Content-Type': 'application/json',
      // 'Authorization': 'Bearer '+this.tokenService.getToken()!
      // // 'Access-Control-Allow-Origin': '*'
      // }}
      );
  }

  addProductToCart(product: Product) {
    // var produsVenit = this.http.post(postToCartUrl, product);
    // return produsVenit;
    // product["pid"] =product.id;
    let data= {
      "pid":product.id
    }

    //DE CE NU MERGE AICI FARA HEADER???
    let config ={ headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer '+this.tokenService.getToken()!
      // 'Access-Control-Allow-Origin': '*'
      }
    }
    axios.post(postToCartUrl, data, config)
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
  
  }
  
}
