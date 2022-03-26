import { Component, Input, OnInit } from '@angular/core';
import { Product } from 'src/app/models/product.model';
import { CartService } from 'src/app/services/cart.service';
import { MessengerService } from 'src/app/services/messenger.service';

@Component({
  selector: 'app-product-item',
  templateUrl: './product-item.component.html',
  styleUrls: ['./product-item.component.scss']
})
export class ProductItemComponent implements OnInit {

  @Input() public productItem={} as Product;

  constructor(private msg: MessengerService, private cartService: CartService) { 
  }

  ngOnInit(): void {
  }

  handleAddToCart(){
    this.cartService.addProductToCart(this.productItem).subscribe(()=>{
      this.msg.sendMsg(this.productItem)
    })
    
  }
}
