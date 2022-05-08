import { Component, Input, OnInit } from '@angular/core';
import { Product } from '../models/product.model';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-individual-product',
  templateUrl: './individual-product.component.html',
  styleUrls: ['./individual-product.component.scss']
})
export class IndividualProductComponent implements OnInit {

  @Input() public product={} as Product;
  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    // this.loadProduct();
  }

  // loadProduct(){
  //   this.productService.get(this.product.id).subscribe((productItem) =>{this.product=productItem})
  // }

}
