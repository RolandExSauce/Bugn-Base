import { apiClient } from "../api/api-client";
import type { CreateReviewRequest, Review } from "../types/models";

class ReviewService {
  public static getByProduct = async (productId: number): Promise<Review[]> => {
    return apiClient.get<Review[]>(`/reviews/product/${productId}`);
  };

  public static create = async (payload: CreateReviewRequest): Promise<Review> => {
    return apiClient.post<Review>(`/reviews`, payload);
  };
}

export default ReviewService;
