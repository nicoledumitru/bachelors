import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Product } from '../models/product.model';
import { Review } from '../models/review-data';
import { ProductService } from '../services/product.service';
import { ReviewService } from '../services/review.service';
import { TokenStorageServiceService } from '../services/token-storage-service.service';

@Component({
  selector: 'app-individual-product',
  templateUrl: './individual-product.component.html',
  styleUrls: ['./individual-product.component.scss']
})
export class IndividualProductComponent implements OnInit {

  max = 5;
  rate = 2;
  isReadonly = false;
  reviewsList: Review[]=[]

  private reviewData={} as Review;

  reviewForm= new FormGroup({
    'text': new FormControl('',[Validators.required]),
    'rating': new FormControl('',[Validators.required])
  })

  @Input() public product={} as Product;
  constructor(private productService: ProductService,
    private reviewService: ReviewService) { }

  ngOnInit(): void {
    let urlParts = document.URL.split("/");
    this.loadProduct(parseInt(urlParts[urlParts.length-1]));
    this.extractReviews(parseInt(urlParts[urlParts.length-1]));
  }

  loadProduct(productId: number){
    this.productService.get(productId).subscribe((productItem) =>{this.product=productItem})
  }

  extractReviews(productId: number){
    this.reviewService.getAll(productId).subscribe((reviews)=>{
      this.reviewsList= reviews;
    });
  }

  addReview(){
    this.reviewData.text = this.reviewForm.controls['text'].value;
    this.reviewData.rating = this.rate;

    this.reviewService.addReview(this.reviewData)
  }

}
