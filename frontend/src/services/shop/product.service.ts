import { apiClient } from "../../api/api-client";
import type { Product, ProductDTO, ProductFilter } from "../../types/models";


export class ProductService {
    // Admin endpoints
    static async getAdminProduct(id: string): Promise<Product> {
        return apiClient.get<Product>(`/api/admin/product/${id}`);
    }

    static async getAdminProducts(): Promise<Product[]> {
        return apiClient.get<Product[]>('/api/admin/products');
    }

    static async addProduct(product: ProductDTO): Promise<Product> {
        return apiClient.post<Product>('/api/admin/add-product', product);
    }

    static async updateProduct(id: string, product: ProductDTO): Promise<void> {
        return apiClient.put(`/api/admin/update-product/${id}`, product);
    }

    static async deleteProduct(id: string): Promise<void> {
        return apiClient.delete(`/api/admin/delete-product/${id}`);
    }

    // Shop endpoints (public)
    static async getProduct(id: string): Promise<Product> {
        return apiClient.get<Product>(`/api/shop/products/${id}`);
    }

    static async getProducts(filter?: ProductFilter): Promise<Product[]> {
        return apiClient.get<Product[]>('/api/shop/products', filter);
    }
}