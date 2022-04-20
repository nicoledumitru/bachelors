import { Component, OnInit, ViewChild} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/services/product.service';
import { TokenStorageServiceService } from 'src/app/services/token-storage-service.service';

@Component({
  selector: 'app-add-product-modal',
  templateUrl: './add-product-modal.component.html',
  styleUrls: ['./add-product-modal.component.scss']
})
export class AddProductModalComponent implements OnInit {

  productForm = new FormGroup({
    'name' : new FormControl('',[Validators.required]),
    // 'topics' : new FormControl([],[Validators.required]),
    'description' : new FormControl('',[Validators.required]),
    'price': new FormControl('',[Validators.required]),
  })
  private productData= {} as Product;
  private fileName = '';
  constructor(public dialog: MatDialog,
    private productService: ProductService,
    private tokenStorageService : TokenStorageServiceService) { }

  ngOnInit(): void {
    this.productService.getAll().subscribe( res =>{
      console.log(res);
    })
  }

  closeModal(){
    const CancelButtonRef = this.dialog.closeAll()
  }

  saveData(){
    this.productData.name = this.productForm.controls['name'].value;
    this.productData.description = this.productForm.controls['description'].value;
    this.productData.price = 0;
    this.productData.userName = this.tokenStorageService.getAnswerLogin().username;
    this.productForm.controls['name'].setValue('');
    this.productForm.controls['description'].setValue('');
    this.productForm.controls['price'].setValue('');
    
    const CancelButtonRef = this.dialog.closeAll()
    this.productService.create(this.productData).subscribe( res => {
      console.log(res);
    }); 
  }

  timeConvertor(UNIX_timestamp: number) : string{
    let aux = new Date(UNIX_timestamp);
    var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    let year = aux.getFullYear();
    let mounth = months[aux.getMonth()];
    let date = aux.getDate();
    let hour = aux.getHours();
    let min = aux.getMinutes();

    let time = date + ' ' + mounth + ' ' + year + ' ' + hour + ':' + min;
    return time;
  }

}
