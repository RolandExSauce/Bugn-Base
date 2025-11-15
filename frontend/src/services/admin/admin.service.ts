import type { User, Product } from "../../types/models";

const BASE_URL = import.meta.env.VITE_BASE_URL;

// TODO: omit return type, implement status check. if not ok, throw error (component caches it),
// otherwise return void or return await repsonse.json()

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

  public static updateOrder = async (orderStatus: string): Promise<void> => {
    // TODO: implement update order status request
    return;
  };
}
