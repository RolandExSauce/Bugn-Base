import { apiClient } from "../api/api-client";
import type { User } from "../types/models";

export type AdminUpdateUserDto = {
  firstname: string;
  lastname: string;
  phone?: string | number;
  address?: string;
  postcode: number;
  email: string;
  active: boolean;
  role: "ROLE_USER" | "ROLE_ADMIN";
};

class AdminUserService {
  public static getAllUsers = async (): Promise<User[]> => {
    return apiClient.get<User[]>("/admin/users");
  };

  public static getUserById = async (id: string): Promise<User> => {
    return apiClient.get<User>(`/admin/users/${id}`);
  };

  public static updateUser = async (
    id: string,
    dto: AdminUpdateUserDto
  ): Promise<User> => {
    return apiClient.put<User>(`/admin/users/${id}`, dto);
  };

  public static deleteUser = async (id: string): Promise<void> => {
    return apiClient.delete<void>(`/admin/users/${id}`);
  };
}

export default AdminUserService;
