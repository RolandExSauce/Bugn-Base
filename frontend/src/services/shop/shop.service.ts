import { apiClient } from "../../api/api-client";
import type { Product, ProductCategory, ProductFilter } from "../../types/models";

class ShopService {

  public static getProduct = async (id: string): Promise<Product> => {
        return apiClient.get<Product>(`/api/shop/products/product/${id}`);
  };

  public static getProducts = async (
    filters?: ProductFilter
  ): Promise<Product[]> => {
    const backendFilters = {
      name: filters?.name,
      category: filters?.category,
      priceMin: filters?.priceMin,
      priceMax: filters?.priceMax,
      brand: filters?.brand,
      pageNo: filters?.pageNumber,
      pageSize: filters?.pageSize
    };

    return apiClient.get<Product[]>("/api/shop/products", backendFilters);

  };

  public static getCategories = async (): Promise<ProductCategory[]> => {
   return apiClient.get<ProductCategory[]>("/api/shop/products/categories");
  };
}
export default ShopService;