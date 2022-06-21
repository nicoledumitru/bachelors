import { Product } from "./product.model";

export class CartItem {
  id?: number;
  productId?: number;
  productName?: string;
  productImage?: string;
  quantity: number;
  price?: number;
  userName?: String;

  constructor(id: number, product: Product, qty = 1) {
    this.id = id;
    this.productId = product.id;
    this.productName = product.name;
    this.productImage = product.imageUrl;
    this.price = product.price;
    this.quantity = qty;
  }
}