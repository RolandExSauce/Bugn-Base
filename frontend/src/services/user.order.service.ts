import { apiClient } from "../api/api-client";
import type { Order, OrderStatus } from "../types/models";

class UserOrderService {
  public static createOrder = async (
    order: Partial<Order>
  ): Promise<OrderStatus> => {
    return apiClient.post<OrderStatus>("/user/orders", order);
  };

  public static getOrderById = async (id: number): Promise<Order> => {
    const res = await apiClient.get<Order>(`/user/orders/${id}`);
    return res;
  };

  public static getOrdersForCustomer = async (): Promise<Order[]> => {
    const res = await apiClient.get<Order[]>(`/user/orders/customer`);
    return res;
  };

  public static cancelOrder = async (id: number): Promise<OrderStatus> => {
    const res = await apiClient.patch<OrderStatus>(`/user/orders/${id}/cancel`);
    return res;
  };

  public static returnOrder = async (id: number): Promise<OrderStatus> => {
    const res = await apiClient.patch<OrderStatus>(`/user/orders/${id}/return`);
    return res;
  };
}

export default UserOrderService;
