import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AddProductModalComponent } from '../add-product-modal/add-product-modal.component';
import { TokenStorageServiceService } from '../services/token-storage-service.service';
import { Chart } from 'chart.js';
// import { Color, Label } from 'ng2-charts';
import { OrderService } from '../services/order.service';
import { Order } from '../models/order';
import { ProductService } from '../services/product.service';
import { Product } from '../models/product.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  currentUser: any;
  isAdmin: boolean=false;
  private roles: string[] = [];

  orderList: Order[]=[];
  productList: Product[]=[];

  result: any={};
  noOfOrders: any;
  months=['January', 'February', 'March', 'April', 'May', 'June'];
  lineChart: any=[]
  barChart: any=[]

  constructor(private tokenService: TokenStorageServiceService, private router: Router,
    public dialog: MatDialog,
    private orderService: OrderService,
    private productService: ProductService) { }

  ngOnInit(): void {
    this.currentUser = this.tokenService.getAnswerLogin();
    this.roles = this.currentUser.roles;
    this.isAdmin = this.roles.includes('ROLE_ADMIN');

    if(this.isAdmin){
      this.getAdminBuyers();
      this.getAdminRecommendations();
    } else{
      this.getCustomerOrders();
    }
  }

  logout(): void {
    this.tokenService.signOut();
    // window.location.reload();
    this.router.navigateByUrl('');
  }

  openModalAddProduct(){
    const dialogRef = this.dialog.open(AddProductModalComponent, {
      height : '600px',
      width: '600px'
    });
  }

  getCustomerOrders(){
    this.orderService.getAll().subscribe((orders)=>{
      this.orderList = orders;
      console.log(this.orderList);
    });
  }

  getAdminBuyers(){
    {
      this.orderService.getNoOfBuyers().subscribe((sum)=>{
        this.result = sum;
        console.log(this.result);

        this.lineChart = new Chart('canvas', {
          type: 'line',
          data:{
            labels: this.months,
            datasets:[
             {
                label:'Number of Orders',
                data: [12,5,8,10,25,20],
                borderWidth:1,
                backgroundColor: 'rgba(93,175,89,0.1)'
              },
            ],
          },
          options: {
            maintainAspectRatio: false,
            responsive: true,
            hover: {
              mode: "nearest",
              intersect: true,
            },
          },
        });

        this.barChart = new Chart('canvas2', {
          type: 'bar',
          data:{
            labels: this.months,
            datasets:[
             {
                label:'Number of Orders',
                data: [12,5,8,10,25,20],
                borderWidth:1,
                backgroundColor: 'rgba(93,175,89,0.1)'
              },
            ],
          },
          options: {
            maintainAspectRatio: false,
            responsive: true,
            hover: {
              mode: "nearest",
              intersect: true,
            },
          },
        })
      });
    }
  }

  getAdminRecommendations(){
    this.productService.adminRecommendations().subscribe((products)=>{
      this.productList = products;
      console.log(this.productList);
    });
  }
}
