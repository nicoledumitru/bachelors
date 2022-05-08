import { ImageModel } from "./image-model";

export class Product {
    id?: number;
    name?: string;
    description?: string;
    price?: number;
    picture?: any;
    type?: number;
    userName?: string;
    stock?: number;
    pid?:number;

    constructor(id: number, imageModel: ImageModel) {
        this.id = id;
        this.picture = imageModel;
    }
}