import type ProductDto from "./ProductDto";

export default interface CartItemDto {
  product: ProductDto;
  amount: number;
}
