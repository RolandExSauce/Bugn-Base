import { apiClient } from "../../api/api-client";
import type { Order, OrderDTO, OrderStatus } from "../../types/models";

class AdminOrderService {

  public static getAllOrders = async (): Promise<Order[]> => {
    return apiClient.get<Order[]>("/admin/orders");
  };

  public static getOrderById = async (id: number): Promise<OrderDTO> => {
    return apiClient.get<OrderDTO>(`/admin/orders/${id}`);
  };

  public static updateOrderStatus = async (
    id: number,
    status: OrderStatus
  ): Promise<OrderStatus> => {
    return apiClient.patch<OrderStatus>(
      `/admin/orders/${id}/status`,
      null,
      { params: { status } }
    );
  };

  public static deleteOrder = async (id: number): Promise<void> => {
    return apiClient.delete<void>(`/admin/orders/${id}`);
  };
}
export default AdminOrderService;