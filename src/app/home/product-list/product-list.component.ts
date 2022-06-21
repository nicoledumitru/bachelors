import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {

  productList: Product[] = []
  wishlistItems: Product[] = [];
  currentCategoryId = -1;
  currentProduct: Product = {};
  name = '';
  pageNo: number = 0;

  private isSorting: boolean = false;

  constructor(private productService: ProductService, private router: Router, private ref: ChangeDetectorRef) {

  }

  ngOnInit() {
    if (document.URL.includes('sort')) {
      this.isSorting = true;
      let urlParts = document.URL.split("/");
      this.currentCategoryId = parseInt(urlParts[urlParts.length - 1]);
      this.sortProducts(this.currentCategoryId);
    }
    else {
      this.isSorting = false;
      this.productService.getAll(this.pageNo).subscribe((products) => {
        this.productList = products;
      });
    }
  }

  sortProducts(currentCategoryId: number) {
    this.productService.sortingProducts(currentCategoryId, this.pageNo).subscribe(
      data => {
        this.productList = data;
        console.log(data);
      }
    )
  }

  searchProductsByName(name: string) {
    this.productService.findByName(name).subscribe(
      data => {
        this.productList = data;
        console.log(data);
      }
    )
  }

  changePageNumber(increase: boolean) {
    if (increase) {
      this.pageNo++;
    } else if (this.pageNo > 0) {
      this.pageNo--;
    }
    console.log(this.isSorting +  " " + this.pageNo + " " + this.currentCategoryId)
    if (this.isSorting) {
      this.sortProducts(this.currentCategoryId);
    } else {
      this.productService.getAll(this.pageNo).subscribe((products) => {
        this.productList = products;
      });
    }
  }
}
