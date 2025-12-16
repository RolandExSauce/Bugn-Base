import { apiClient } from "../../api/api-client";
import type { Product, ProductCategory, ProductFilter } from "../../types/models";

class ShopService {
  public static getProduct = async (id: string): Promise<Product> => {
    return apiClient.get<Product>(`/api/shop/products/product/${id}`);
  };

  public static getProducts = async (
    filters?: ProductFilter
  ): Promise<Product[]> => {
    // Build query parameters properly
    const params = new URLSearchParams();
    
    if (filters?.name) params.append('name', filters.name);
    if (filters?.category) params.append('category', filters.category);
    if (filters?.priceMin !== undefined) params.append('priceMin', filters.priceMin.toString());
    if (filters?.priceMax !== undefined) params.append('priceMax', filters.priceMax.toString());
    
    // Handle array of brands
    if (filters?.brand && filters.brand.length > 0) {
      filters.brand.forEach(brand => {
        params.append('brand', brand);
      });
    }
    
    // Add pagination with defaults
    params.append('pageNo', (filters?.pageNumber || 0).toString());
    params.append('pageSize', (filters?.pageSize || 20).toString());
    
    // Make request with params
    return apiClient.get<Product[]>(`/api/shop/products?${params.toString()}`);
  };

  public static getCategories = async (): Promise<ProductCategory[]> => {
    return apiClient.get<ProductCategory[]>("/api/shop/products/categories");
  };
}

export default ShopService;