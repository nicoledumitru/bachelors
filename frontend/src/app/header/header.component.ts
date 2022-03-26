import { Component, OnInit } from '@angular/core';
import { Product } from '../models/product.model';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  products?: Product[];
  currentProduct: Product = {};
  currentIndex = -1;
  name = '';

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
  }

  searchTitle(): void {
    this.currentProduct = {};
    this.currentIndex = -1;

    this.productService.findByName(this.name)
      .subscribe({
        next: (data) => {
          this.products = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }

}
