import type { User } from "../../types/models";

const BASE_URL = import.meta.env.VITE_BASE_URL;

export default class AdminService {
  public static updateUser = async (user: User): Promise<User> => {
    // TODO: implement PUT request
    return user;
  };

  public static deleteUser = async (id: string): Promise<void> => {
    // TODO: implement DELETE request
    return;
  };
}
