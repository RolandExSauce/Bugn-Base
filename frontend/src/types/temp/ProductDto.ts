import type { ProductCategory } from "./ProductCategory";

export default interface ProductDto {
  productId: string;
  name: string;
  category: ProductCategory;
  description: string;
  price: number;
  shippingCost: number;
  brand: string;
  stockStatus: boolean;
  shippingTime: number;
  active: boolean;
}
