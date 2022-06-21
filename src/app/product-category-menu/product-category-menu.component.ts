import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProductCategory } from '../models/product-category';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-product-category-menu',
  templateUrl: './product-category-menu.component.html',
  styleUrls: ['./product-category-menu.component.scss']
})
export class ProductCategoryMenuComponent implements OnInit {

  productCategories: ProductCategory[]=[];
  currentCategory= -1;
  
  constructor(private productService: ProductService, private router: Router) { }

  ngOnInit() {
    this.productService.getProductCategories().subscribe((categories)=>{
      this.productCategories= categories;
    });
  }

  setActiveCategory(category: number): void{ 
    this.currentCategory= category;
    console.log(category);
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>
    this.router.navigate(['/products/sort/'+category]));
  }
  
}
