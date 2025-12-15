import { apiClient } from "../../api/api-client";
import type { OrderDTO, OrderStatus } from "../../types/models";

class UserOrderService {

  public static createOrder = async (order: OrderDTO): Promise<OrderStatus> => {
    return apiClient.post<OrderStatus>("/user/orders", order);
  };

  public static getOrderById = async (
    id: number,
    email: string
  ): Promise<OrderDTO> => {
    const res = await apiClient.get<OrderDTO>(`/user/orders/${id}`, { email });
    return res;
  };

  public static getOrdersForCustomer = async (
    email: string
  ): Promise<OrderDTO[]> => {
    const res = await apiClient.get<OrderDTO[]>(`/user/orders/customer/${email}`);
    return res;
  };

  public static cancelOrder = async (
    id: number,
    email: string
  ): Promise<OrderStatus> => {
    const res = await apiClient.patch<OrderStatus>(
      `/user/orders/${id}/cancel`,
      null,
      { params: { email } }
    );
    return res; 
  };

  public static returnOrder = async (
    id: number,
    email: string
  ): Promise<OrderStatus> => {
    const res = apiClient.patch<OrderStatus>(
      `/user/orders/${id}/return`,
      null,
      { params: { email } }
    );
    return res; 
  };

}
export default UserOrderService;