import { apiClient } from "../api/api-client";
import type { Product, ProductCategory, ProductFilter } from "../types/models";

class ShopService {
  public static getProduct = async (id: string): Promise<Product> => {
    return apiClient.get<Product>(`/products/${id}`);
  };

  public static getProducts = async (
    filters?: ProductFilter
  ): Promise<Product[]> => {
    const params = new URLSearchParams();

    if (filters?.name) params.append("name", filters.name);

    if (filters?.category) params.append("category", filters.category);

    if (filters?.priceMin != null) params.append("priceMin", String(filters.priceMin));
    if (filters?.priceMax != null) params.append("priceMax", String(filters.priceMax));

    if (Array.isArray(filters?.brand) && filters!.brand.length > 0) {
      filters!.brand.forEach((brand) => {
        if (brand) params.append("brand", brand);
      });
    }

    if (filters?.stars != null) params.append("stars", String(filters.stars));

    // ✅ Backend erwartet pageNo + pageSize (so wie dein Controller)
    params.append("pageNo", String(filters?.pageNumber ?? 0));
    params.append("pageSize", String(filters?.pageSize ?? 20));

    return apiClient.get<Product[]>(`/products?${params.toString()}`);
  };

  public static getCategories = async (): Promise<ProductCategory[]> => {
    return apiClient.get<ProductCategory[]>("/products/categories");
  };
}

export default ShopService;
