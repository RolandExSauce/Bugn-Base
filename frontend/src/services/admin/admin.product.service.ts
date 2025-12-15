import { apiClient } from "../../api/api-client";
import type { Product, ProductDTO } from "../../types/models";

class AdminProductService {

  public static getProduct = async (id: string): Promise<Product> => {
       return apiClient.get<Product>(`/api/admin/product/${id}`);
  };

  public static getProducts = async (): Promise<Product[]> => {
       return apiClient.get<Product[]>("/api/admin/products");
  };

  public static addProduct = async (product: ProductDTO): Promise<Product> => {
       return apiClient.post<Product>("/api/admin/add-product", product);
  };

  public static updateProduct = async (
    id: string,
    product: ProductDTO
  ): Promise<void> => {
    return apiClient.put<void>(`/api/admin/update-product/${id}`, product);
  };

  public static deleteProduct = async (id: string): Promise<void> => {
       return apiClient.delete<void>(`/api/admin/delete-product/${id}`);
  };
}
export default AdminProductService;