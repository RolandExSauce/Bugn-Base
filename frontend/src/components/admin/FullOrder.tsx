import { useEffect, useRef, useState } from "react";
import type { DeliveryStatus, Order } from "../../types/models";
import AdminDeleteButton from "../common/AdminDeleteButton";
import AdminUpdateButton from "../common/AdminUpdateButton";
import AdminService from "../../services/admin/admin.order.service";

type FullOrderProps = {
  order: Order;
};

export default function FullOrder({ order }: FullOrderProps) {
  const trRef = useRef<HTMLTableRowElement>(null);

  const [updateOrderStatus, setUpdateOrderStatus] = useState(
    order.deliveryStatus
  );

  useEffect(() => {
    setUpdateOrderStatus(order.deliveryStatus);
  }, [order]);

  const handleStatusChange = (
    e: React.ChangeEvent<HTMLSelectElement>
  ): void => {
    setUpdateOrderStatus(e.target.value as DeliveryStatus);
  };

  const handleSave = () => {
    AdminService.updateOrder(updateOrderStatus);

    // success:
    trRef.current?.classList.remove("user-row-success");
    void trRef.current?.offsetWidth;
    trRef.current?.classList.add("user-row-success");
  };

  const handleDelete = () => {};

  return (
    <tr ref={trRef} key={order.id}>
      <td>{order.id}</td>
      <td>{order.user.firstname}</td>
      <td>{new Date(order.orderDate).toLocaleDateString()}</td>
      <td>${order.totalAmount.toFixed(2)}</td>
      <td>
        {order.deliveryFullname} <br />
        {order.deliveryAddress} <br />
        {order.deliveryPostcode}
      </td>
      <td>
        <select
          className="form-select"
          value={updateOrderStatus}
          onChange={handleStatusChange}
        >
          <option value="pending">pending</option>
          <option value="shipped">shipped</option>
          <option value="delivered">delivered</option>
          <option value="cancelled">cancelled</option>
        </select>
      </td>
      <td>{order.paymentMethod}</td>
      <td>
        <div className="d-flex flex-column gap-2">
          {order.items.map((item, index) => (
            <div key={index}>
              {item.quantity} × {item.product.name}
            </div>
          ))}
        </div>
      </td>
      <td>
        <AdminUpdateButton
          disabled={updateOrderStatus === order.deliveryStatus}
          action={handleSave}
        />
        <AdminDeleteButton action={handleDelete} />
      </td>
    </tr>
  );
}
