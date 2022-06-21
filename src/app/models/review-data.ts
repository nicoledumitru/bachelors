import { AnswerLogin } from "./answerLogin";
import { Product } from "./product.model";

export class Review {
    product?: Product;
    user?: AnswerLogin;
    rating?: number;
    text?: string;
}