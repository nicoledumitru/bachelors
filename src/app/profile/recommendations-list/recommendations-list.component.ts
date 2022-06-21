import { Component, OnInit } from '@angular/core';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/services/product.service';
import { TokenStorageServiceService } from 'src/app/services/token-storage-service.service';

@Component({
  selector: 'app-recommendations-list',
  templateUrl: './recommendations-list.component.html',
  styleUrls: ['./recommendations-list.component.scss']
})
export class RecommendationsListComponent implements OnInit {
  currentUser: any;
  isAdmin: boolean=false;
  private roles: string[] = [];

  recommendationList: Product[]=[]
  constructor(private productService: ProductService, private tokenService: TokenStorageServiceService) { }

  ngOnInit(): void {
    this.currentUser = this.tokenService.getAnswerLogin();
    this.roles = this.currentUser.roles;
    this.isAdmin = this.roles.includes('ROLE_ADMIN');

    if(this.isAdmin){
      this.productService.adminRecommendations().subscribe((products)=>{
        this.recommendationList= products;
      });
    } else{
      this.productService.userRecommendations().subscribe((products)=>{
        this.recommendationList= products;
      });
    }
  }

}
