import { ImageModel } from "./image-model";
import { ProductCategory } from "./product-category";

export class Product {
    id?: number;
    name?: string;
    description?: string;
    price?: number;
    imageUrl?: string;
    type?: ProductCategory;
    userName?: string;
    stock?: number;
    pid?:number;

    constructor(id: number) {
        this.id = id;
        // this.type?.categoryName= category;
    }
}