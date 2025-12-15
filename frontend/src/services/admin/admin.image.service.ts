import { apiClient } from "../../api/api-client";
import type { Image } from "../../types/models";

class AdminImageService {

  public static uploadImages = async (
    productId: string,
    files: File[]
  ): Promise<string> => {
    const formData = new FormData();
    files.forEach((file) => {
      formData.append("files", file);
    });
    return apiClient.postFormData<string>(
    `/api/admin/products/${productId}/images`,
      formData
    );
  };

  public static getProductImages = async (
    productId: string
  ): Promise<Image[]> => {
    return apiClient.get<Image[]>(`/api/admin/products/${productId}/images`);
  };

  public static deleteImage = async (imageId: string): Promise<void> => {
     return apiClient.delete<void>(`/api/admin/products/images/${imageId}`);
  };
}
export default AdminImageService;