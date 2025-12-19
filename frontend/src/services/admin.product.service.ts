import { apiClient } from "../api/api-client";
import type { Product, ProductDTO } from "../types/models";

class AdminProductService {
  public static getProduct = async (id: string): Promise<Product> => {
    return apiClient.get<Product>(`/admin/product/${id}`);
  };

  public static getProducts = async (): Promise<Product[]> => {
    return apiClient.get<Product[]>("/admin/products");
  };

  public static addProduct = async (product: ProductDTO): Promise<Product> => {
    return apiClient.post<Product>("/admin/add-product", product);
  };

  public static updateProduct = async (
    id: string,
    product: ProductDTO
  ): Promise<void> => {
    return apiClient.put<void>(`/admin/update-product/${id}`, product);
  };

  public static deleteProduct = async (id: string): Promise<void> => {
    return apiClient.delete<void>(`/admin/delete-product/${id}`);
  };

  public static addImage = async (
    product: Product,
    file: File
  ): Promise<string> => {
    const formData = new FormData();
    formData.append("file", file);

    return apiClient.post<string>(`/media/file/${product.id}`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  };

  public static deleteImage = async (imageUrl: string): Promise<void> => {
    const path = imageUrl.replace("/media/", "");
    await apiClient.delete(`/media/file/delete${path}`);
  };
}
export default AdminProductService;
