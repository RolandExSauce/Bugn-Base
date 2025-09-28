import type { ProductCategory } from "./ProductCategory";

export default interface ProductDto {
  product_id: string;
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
