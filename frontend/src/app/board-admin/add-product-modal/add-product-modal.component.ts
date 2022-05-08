import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/services/product.service';
import { TokenStorageServiceService } from 'src/app/services/token-storage-service.service';
import { UploadFileService } from 'src/app/services/upload-file.service';

@Component({
  selector: 'app-add-product-modal',
  templateUrl: './add-product-modal.component.html',
  styleUrls: ['./add-product-modal.component.scss']
})
export class AddProductModalComponent implements OnInit {

  selectedFile:any;
  event1:any;
  imgURL: any;
  receivedImageData: any;
  base64Data: any;
  convertedImage: any;

  products: Product[]=[];

  productForm = new FormGroup({
    'name' : new FormControl('',[Validators.required]),
    // 'topics' : new FormControl([],[Validators.required]),
    'description' : new FormControl('',[Validators.required]),
    'price': new FormControl('',[Validators.required]),
    'type': new FormControl('',[Validators.required]),
    'stock': new FormControl('',[Validators.required])
  })
  private productData= {} as Product;
  // private fileName = '';
  constructor(public dialog: MatDialog,
    private productService: ProductService,
    private tokenStorageService : TokenStorageServiceService, 
    private uploadService: UploadFileService) { }

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
    this.productData.price = this.productForm.controls['price'].value;
    this.productData.stock = this.productForm.controls['stock'].value;
    this.productData.userName = this.tokenStorageService.getAnswerLogin().username;
    this.productForm.controls['name'].setValue('');
    this.productForm.controls['description'].setValue('');
    this.productForm.controls['price'].setValue('');
    this.productForm.controls['stock'].setValue('');
    
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

  onUpload() {
    const uploadData = new FormData();
    uploadData.append('myFile', this.selectedFile, this.selectedFile.name);
    try{
      this.uploadService.onUpload(uploadData)
    } catch(error) {
      console.log(error);
    }
    // this.productData.imageUrl= uploadData.toString();
  
  
  //   this.httpClient.post('http://localhost:8080/check/upload', uploadData)
  //   .subscribe(
  //                res => {console.log(res);
  //                        this.receivedImageData = res;
  //                        this.base64Data = this.receivedImageData.pic;
  //                        this.convertedImage = 'data:image/jpeg;base64,' + this.base64Data; },
  //                err => console.log('Error Occured duringng saving: ' + err)
  //             );
   }

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

  changeType(e: any) {
    // this.productData.type = this.productForm.controls['type'].setValue(e.target.value, {
    //   onlySelf: true,
    // });
  }

}
