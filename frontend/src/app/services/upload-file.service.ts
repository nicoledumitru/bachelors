import { Injectable } from '@angular/core';
import axios from 'axios';
import { TokenStorageServiceService } from './token-storage-service.service';
const uploadImage = "http://localhost:8080/products/upload";

@Injectable({
  providedIn: 'root'
})
export class UploadFileService {

  // selectedFile:any;

  constructor(private tokenService: TokenStorageServiceService) { }

  onUpload(data: any) {
    // const uploadData = new FormData();
    // uploadData.append('myFile', this.selectedFile, this.selectedFile.name);

    let config ={ headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer '+this.tokenService.getToken()!
      // 'Access-Control-Allow-Origin': '*'
      }
    }
    axios.post(uploadImage, data, config)
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
  }
}
