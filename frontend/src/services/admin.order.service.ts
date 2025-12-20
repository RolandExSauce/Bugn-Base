import { apiClient } from "../api/api-client";
import type { Order, OrderStatus } from "../types/models";

class AdminOrderService {
  public static getAllOrders = async (): Promise<Order[]> => {
    return apiClient.get<Order[]>("/admin/orders");
  };

  public static getOrderById = async (id: number): Promise<Order> => {
    return apiClient.get<Order>(`/admin/orders/${id}`);
  };

  public static updateOrder = async (order: Order): Promise<Order> => {
    return apiClient.patch<Order>(`/admin/orders/update`, order);
  };

  public static deleteOrder = async (id: number): Promise<void> => {
    return apiClient.delete<void>(`/admin/orders/delete/${id}`);
  };
}
export default AdminOrderService;
