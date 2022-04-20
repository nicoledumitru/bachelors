import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../models/product.model';
import { ProductService } from '../services/product.service';
import { TokenStorageServiceService } from '../services/token-storage-service.service';

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

  isLoggedIn = false;
  username?: string;

  constructor(private productService: ProductService, private tokenService: TokenStorageServiceService, private router:Router) { }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenService.getToken();
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

  choosePage(): void{
    if (this.isLoggedIn) {
      this.router.navigateByUrl('/profile');
    }
    else{
      this.router.navigateByUrl('/login');
    }
  }

}
