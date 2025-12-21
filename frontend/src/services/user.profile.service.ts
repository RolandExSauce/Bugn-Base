import { apiClient } from "../api/api-client";
import type { User } from "../types/models";

export default class UserProfileService {
  public static getUserProfile = async (): Promise<any> => {
    return apiClient.get<any>("/user/profile");
  };

  public static updateUserProfile = async (
    profileData: Partial<User>
  ): Promise<User> => {
    return apiClient.patch<any>("/user/profile", profileData);
  };
}
