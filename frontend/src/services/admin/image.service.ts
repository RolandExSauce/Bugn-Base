import { apiClient } from "../../api/api-client";
import type { Image } from "../../types/models";

export class ImageService {
    static async uploadImages(productId: string, files: File[]): Promise<string> {
        const formData = new FormData();
        files.forEach(file => {
            formData.append('files', file);
        });

        return apiClient.postFormData<string>(
            `/api/admin/products/${productId}/images`,
            formData
        );
    }

    static async getImages(productId: string): Promise<Image[]> {
        return apiClient.get<Image[]>(`/api/admin/products/${productId}/images`);
    }

    static async deleteImage(productId: string, imageId: string): Promise<void> {
        return apiClient.delete(`/api/admin/products/${productId}/images/${imageId}`);
    }
}