import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import axios from 'axios';
import { map, Observable } from 'rxjs';
import { Product } from '../models/product.model';
import { TokenStorageServiceService } from './token-storage-service.service';

const getWishlistURL = 'http://localhost:8080/wishlist';
const addToWishlistURL = 'http://localhost:8080/wishlist/add';
const deleteFromWishlistURL = 'http://localhost:8080/wishlist/delete'

@Injectable({
  providedIn: 'root'
})
export class WishlistService {

  constructor(private http: HttpClient, private tokenService: TokenStorageServiceService) { }

  addToWishlist(product: Product){
    let data= {
      "pid":product.id
    }
    let config ={ headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer '+this.tokenService.getToken()!
      // 'Access-Control-Allow-Origin': '*'
      }
    }
    // return this.http.post(addToWishlistURL, data)
    axios.post(addToWishlistURL, data, config)
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
  }

  removeFromWishlist(product: Product){
    // return this.http.delete(deleteFromWishlistURL+ '/' +product.id);
    let config ={ headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer '+this.tokenService.getToken()!
      // 'Access-Control-Allow-Origin': '*'
      }
    }
    // return this.http.delete(`${deleteFromWishlistURL}/${product.id}`, config);
    
    axios.delete(deleteFromWishlistURL+'/'+product.id, config);
  }

  getWishlist(){
    return this.http.get(getWishlistURL)
    .pipe(
      map((result:any)=>{
        let productIds: any[] =[]
        result.forEach((item:any)=>productIds.push(item.id))
        return productIds;
      })
    )
  }
}
