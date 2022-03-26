import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class MessengerService {

  subject = new Subject();

  constructor() { }

  sendMsg(product: Product){
    // console.log(product);
    this.subject.next(product); //Triggering an event
  }

  getMsg(){
    return this.subject.asObservable() //return the subject as an Observable
  }
}
