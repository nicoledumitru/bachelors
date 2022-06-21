import { Component, Input, OnInit } from '@angular/core';
import { Product } from 'src/app/models/product.model';
import { CartService } from 'src/app/services/cart.service';
import { MessengerService } from 'src/app/services/messenger.service';
import { ProductService } from 'src/app/services/product.service';
import { WishlistService } from 'src/app/services/wishlist.service';

@Component({
  selector: 'app-product-item',
  templateUrl: './product-item.component.html',
  styleUrls: ['./product-item.component.scss']
})
export class ProductItemComponent implements OnInit {

  @Input() public productItem={} as Product;
  @Input() public addedToWishlist : boolean | undefined;

  constructor(private msg: MessengerService, 
    private cartService: CartService,
    private wishlistService: WishlistService,
    private productService: ProductService) { 
  }

  ngOnInit(): void {
  }

  handleAddToCart(){
    try{
      this.cartService.addProductToCart(this.productItem)
      alert("Item added to your cart!");
    } catch(error) {
      console.log(error);
    }
  }

  handleAddToWishlist() {
    try{
      this.wishlistService.addToWishlist(this.productItem)
      // .subscribe(()=>{
      // this.addedToWishlist = true;})
      
    } catch (error) {console.log(error);}
    this.addedToWishlist = true;
  }

  handleRemoveFromWishlist() {
    // this.wishlistService.removeFromWishlist(this.productItem.id).subscribe(()=>{
    //   this.addedToWishlist = false;
    // })

    try{
      this.wishlistService.removeFromWishlist(this.productItem)
      // .subscribe(()=>{
      // this.addedToWishlist = true;})
    } catch (error) {console.log(error);}
    this.addedToWishlist = false;
  }

  openProduct(){
    try{
      this.productService.get(this.productItem.id).subscribe((product) =>{this.productItem=product})
      console.log(this.productItem)
    } catch (error) {console.log(error);}
  }
}
