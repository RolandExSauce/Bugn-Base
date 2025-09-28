import type ProductDto from "./ProductDto";

export default interface ReviewDto {
  reviewId: string;
  product: ProductDto;
  username: string;
  rating: number;
  comment: string;
  createdAt: Date;
}
