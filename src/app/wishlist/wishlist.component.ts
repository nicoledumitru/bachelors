import { Component, OnInit } from '@angular/core';
import { Product } from '../models/product.model';
import { TokenStorageServiceService } from '../services/token-storage-service.service';
import { WishlistService } from '../services/wishlist.service';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.scss']
})
export class WishlistComponent implements OnInit {

  wishlistItems: Product[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(private wishlistService: WishlistService,
    private tokenService: TokenStorageServiceService) { }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenService.getToken();
    this.loadWishlist();
  }

  loadWishlist(){
    // this.wishlistService.getWishlist().subscribe(productIds => {
    //   console.log(productIds)
    //   this.wishlistItems = productIds
    // })
    this.wishlistService.getWishlist().subscribe( items => {
      
      let wishlistItems2: Product[] = [];
      items.map( (item:any) => wishlistItems2.push(new Product(item.id)));
      this.wishlistItems = wishlistItems2;
      console.log(items);
      // this.calcCartTotal();
    })
  }

}
