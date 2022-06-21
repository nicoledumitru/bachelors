import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Product } from '../models/product.model';
import { ProductCategory } from '../models/product-category';

const baseUrl = 'http://localhost:8080/products';
const sortingUrl = 'http://localhost:8080/products/sort';
const getAllCategoriesForProducts = "http://localhost:8080/products/product-category"

@Injectable({
    providedIn: 'root'
})
export class ProductService{

    constructor(private http: HttpClient) { }

  getAll(page: number = 0): Observable<Product[]> {
    return this.http.get<Product[]>(`${baseUrl}/page/${page}`);
  }

  get(id: any): Observable<Product> {
    return this.http.get(`${baseUrl}/${id}`);
  }

  create(data: any): Observable<any> {
    return this.http.post(baseUrl, data);
  }

  update(id: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/${id}`, data);
  }

  delete(id: any): Observable<any> {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  deleteAll(): Observable<any> {
    return this.http.delete(baseUrl);
  }

  findByName(name: any): Observable<Product[]> {
    return this.http.get<Product[]>(`${baseUrl}?name=${name}`);
  }

  getProductCategories(): Observable<ProductCategory[]> {

    // return this.http.get<GetResponseProductCategory>(getAllCategoriesForProducts).pipe(
    //   map(response => response._embedded.productCategory)
    // );
    return this.http.get<Product[]>(getAllCategoriesForProducts);
  }

  sortingProducts(theCategoryId: number, pageNo : number = 0): Observable<Product[]> {

    // need to build URL based on category id 
    // const searchUrl = `${baseUrl}/search/findByCategoryId?id=${theCategoryId}`;

    // return this.httpClient.get<GetResponse>(searchUrl).pipe(
    //   map(response => response._embedded.products)
    // );
    return this.http.get<Product[]>(`${sortingUrl}/${theCategoryId}/${pageNo}`);
  }

  adminRecommendations(): Observable<Product[]>{
    return this.http.get<Product[]>(`${baseUrl}/recommendations`);
  }

  userRecommendations(): Observable<Product[]>{
    return this.http.get<Product[]>(`${baseUrl}/user-recommendations`);
  }
}

interface GetResponseProductCategory {
  _embedded: {
    productCategory: ProductCategory[];
  }
}