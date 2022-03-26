import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { TokenStorageServiceService } from './token-storage-service.service';
import { map } from 'rxjs/operators';
import { CartItem } from '../models/cart-item';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private getCartUrl: string;
  private postToCartUrl: string;
  private headers: HttpHeaders;
  private params = {} as HttpParams;

  constructor(private http: HttpClient,
    private tokenService: TokenStorageServiceService) { 
    this.getCartUrl = "http://localhost:8080/cart"
    this.postToCartUrl = "http://localhost:8080/cart/add"

    this.headers = new HttpHeaders();
    this.params = new HttpParams();
  }

  getCartItems(): Observable<CartItem[]> {
    return this.http.get<CartItem[]>(this.getCartUrl).pipe(
      map((result: any[]) => {
        let cartItems: CartItem[] = [];

        for(let item of result) {
          let productExists = false

          for (let i in cartItems) {
            if (cartItems[i].productId === item.product.id) {
            cartItems[i].qty++
              productExists = true
              break;
            }
          }
          if (!productExists) {
            cartItems.push(new CartItem(item.id, item.product));
          }
        }
        return cartItems;
      })
    );
  }

  addProductToCart(product: Product): Observable<any>{
    this.params.set('pid', product.id!);
    // this.params.set('qty', number);
    return this.http.post<any>(this.postToCartUrl,{'headers': this.headers});
    // return this.http.post(this.postToCartUrl, { product });
  }
}
