import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit, ViewChild} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/services/product.service';
import { TokenStorageServiceService } from 'src/app/services/token-storage-service.service';
import { ProductCategory } from '../models/product-category';

@Component({
  selector: 'app-add-product-modal',
  templateUrl: './add-product-modal.component.html',
  styleUrls: ['./add-product-modal.component.scss']
})
export class AddProductModalComponent implements OnInit{

  imgURL: any;
  products: Product[]=[];

  categories: ProductCategory[]=[];

  productForm = new FormGroup({
    'name' : new FormControl('',[Validators.required]),
    'type': new FormControl('',[Validators.required]),
    'description' : new FormControl('',[Validators.required]),
    'price': new FormControl('',[Validators.required]),
    'imageUrl': new FormControl('',[Validators.required]),
    'stock': new FormControl('',[Validators.required])
  })
  
  private productData= {} as Product;
  // private fileName = '';
  constructor(public dialogRef: MatDialogRef<AddProductModalComponent>,
    // public dialog: MatDialog,
    private productService: ProductService,
    private tokenStorageService : TokenStorageServiceService) { }

  ngOnInit(): void {
    // this.productService.getAll().subscribe( res =>{
    //   console.log(res);
    // })
    this.getCategories();
  }

  closeModal(){
    this.dialogRef.close();
  }

  async saveData(){
    this.productData.name = this.productForm.controls['name'].value;
    this.productData.type = new ProductCategory();
    this.productData.type!.categoryName = this.productForm.controls['type'].value;
    let typeFilters = this.categories.filter(x=>x.categoryName===this.productData.type!.categoryName);
    if(typeFilters.length>0){
      this.productData.type!.id = typeFilters[0].id;
    }
    this.productData.description = this.productForm.controls['description'].value;
    this.productData.price = this.productForm.controls['price'].value;
    this.productData.imageUrl = this.productForm.controls['imageUrl'].value;
    this.productData.stock = this.productForm.controls['stock'].value;
    this.productData.userName = this.tokenStorageService.getAnswerLogin().username;
    this.productForm.controls['name'].setValue('');
    this.productForm.controls['type'].setValue('');
    this.productForm.controls['description'].setValue('');
    this.productForm.controls['price'].setValue('');
    this.productForm.controls['imageUrl'].setValue('');
    this.productForm.controls['stock'].setValue('');
    
    // const CancelButtonRef = this.dialog.closeAll()
    this.productService.create(this.productData).subscribe(); 

    this.closeModal();
  }

  getCategories(){
    this.productService.getProductCategories().subscribe((categoryList)=>{
      this.categories= categoryList;
    });
  }
  // timeConvertor(UNIX_timestamp: number) : string{
  //   let aux = new Date(UNIX_timestamp);
  //   var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
  //   let year = aux.getFullYear();
  //   let mounth = months[aux.getMonth()];
  //   let date = aux.getDate();
  //   let hour = aux.getHours();
  //   let min = aux.getMinutes();

  //   let time = date + ' ' + mounth + ' ' + year + ' ' + hour + ':' + min;
  //   return time;
  // }

  // onUpload() {
  //   const uploadData = new FormData();
  //   uploadData.append('myFile', this.selectedFile, this.selectedFile.name);
  //   try{
  //     this.uploadService.onUpload(uploadData)
  //   } catch(error) {
  //     console.log(error);
  //   }
    // this.productData.imageUrl= uploadData.toString();
  
  
  //   this.httpClient.post('http://localhost:8080/check/upload', uploadData)
  //   .subscribe(
  //                res => {console.log(res);
  //                        this.receivedImageData = res;
  //                        this.base64Data = this.receivedImageData.pic;
  //                        this.convertedImage = 'data:image/jpeg;base64,' + this.base64Data; },
  //                err => console.log('Error Occured duringng saving: ' + err)
  //             );
  //  }

  // onFileChanged(event: any) {
  //   console.log(event);
  //   this.selectedFile = event.target.files[0];

  //   // Below part is used to display the selected image
  //   let reader = new FileReader();
  //   reader.readAsDataURL(event.target.files[0]);
  //   reader.onload = (event2) => {
  //     this.imgURL = reader.result;
  //   };
  // }

  // changeType(e: any) {
    // this.productData.type = this.productForm.controls['type'].setValue(e.target.value, {
    //   onlySelf: true,
    // });
  // }

}
