import type { ProductCategory } from "./ProductCategory";

export default interface ProductDto {
  product_id: string;
  name: string;
  category: ProductCategory;
  description: string;
  price: number;
  shipping_cost: number;
  brand: string;
  stock_status: boolean;
  shipping_time: number;
  active: boolean;
}
