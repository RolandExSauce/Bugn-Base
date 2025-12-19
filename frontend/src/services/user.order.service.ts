import { apiClient } from "../api/api-client";
import type { OrderDTO, OrderStatus } from "../types/models";

class UserOrderService {
  public static createOrder = async (order: OrderDTO): Promise<OrderStatus> => {
    return apiClient.post<OrderStatus>("/user/orders", order);
  };

  public static getOrderById = async (id: number): Promise<OrderDTO> => {
    const res = await apiClient.get<OrderDTO>(`/user/orders/${id}`);
    return res;
  };

  public static getOrdersForCustomer = async (): Promise<OrderDTO[]> => {
    const res = await apiClient.get<OrderDTO[]>(`/user/orders/customer`);
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
