import { Component, OnInit } from '@angular/core';
import { ProductCategory } from '../models/product-category';
import { Product } from '../models/product.model';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-main-container',
  templateUrl: './main-container.component.html',
  styleUrls: ['./main-container.component.scss']
})
export class MainContainerComponent implements OnInit {
  constructor(private productService: ProductService) { }

  pageNo:number=0;
  productList: Product[]=[]

  ngOnInit(): void {
  }
}
