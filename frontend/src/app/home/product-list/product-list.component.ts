import { Component, OnInit } from '@angular/core';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {

  productList: Product[]=[]
  wishlistItems: Product[] = [];
  // product: Product;

  constructor(private productService: ProductService) { 

  }

  ngOnInit() {
    // console.log(this.productService.getProducts());
    this.productService.getAll().subscribe((products)=>{
      this.productList= products;
    });
  }

}
