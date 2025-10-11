import type { User, Product } from "../../types/models";

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

  public static updateProduct = async (product: Product): Promise<Product> => {
    // TODO: implement PUT request
    return product;
  };

  public static deleteProduct = async (id: string): Promise<void> => {
    // TODO: implement DELETE request
    return;
  };
}
