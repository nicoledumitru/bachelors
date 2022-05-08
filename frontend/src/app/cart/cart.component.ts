import { Component, OnInit } from '@angular/core';
import { CartItem } from '../models/cart-item';
import { CartService } from '../services/cart.service';
import { MessengerService } from '../services/messenger.service';
import { TokenStorageServiceService } from '../services/token-storage-service.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  cartItems: CartItem[] = [];
  cartTotal = 0

  isLoggedIn = false;
  username?: string;

  constructor(private msg: MessengerService,
    private cartService: CartService,
    private tokenService: TokenStorageServiceService) { }

  ngOnInit(){
    this.isLoggedIn = !!this.tokenService.getToken();
    this.handleSubscription();
    this.loadCartItems();
  }

  handleSubscription(){
    this.msg.getMsg().subscribe( product => {
      // debugger;
      this.loadCartItems();
    })
  }

  loadCartItems(){
    this.cartService.getCartItems().subscribe( items => {
      
      let cartItems2: CartItem[] = [];
      items.map( (item:any) => cartItems2.push(new CartItem(item.id, item.product, item.quantity)));
      this.cartItems = cartItems2;
      console.log(items);
      this.calcCartTotal();
    })
  }

  calcCartTotal(){
    this.cartTotal=0
    this.cartItems.forEach(item => {
      this.cartTotal += (item.quantity * item.price!)
    })
  }
}
