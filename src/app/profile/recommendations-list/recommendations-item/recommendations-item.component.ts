import { Component, Input, OnInit } from '@angular/core';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-recommendations-item',
  templateUrl: './recommendations-item.component.html',
  styleUrls: ['./recommendations-item.component.scss']
})
export class RecommendationsItemComponent implements OnInit {

  @Input() public recommendationItem={} as Product;

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
  }

  openProduct(){
    try{
      this.productService.get(this.recommendationItem.id).subscribe((item) =>{this.recommendationItem=item})
      console.log(this.recommendationItem)
    } catch (error) {console.log(error);}
  }

}
