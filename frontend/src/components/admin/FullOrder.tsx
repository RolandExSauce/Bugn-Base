import { useState } from "react";
import type { Order } from "../../types/models";
import AdminDeleteButton from "../common/AdminDeleteButton";
import AdminUpdateButton from "../common/AdminUpdateButton";

type FullOrderProps = {
  order: Order;
};

export default function FullOrder({ order }: FullOrderProps) {
  const [currentOrder, setCurrentOrder] = useState(order);

  const handleStatusChange = (
    e: React.ChangeEvent<HTMLSelectElement>
  ): void => {
    const newStatus = e.target.value as Order["deliveryStatus"];
    setCurrentOrder((prev) => ({ ...prev, deliveryStatus: newStatus }));
  };

  const handleSave = () => {};

  const handleDelete = () => {};

  return (
    <tr key={currentOrder.id}>
      <td>{currentOrder.id}</td>
      <td>{currentOrder.user.firstname}</td>
      <td>{new Date(currentOrder.orderDate).toLocaleDateString()}</td>
      <td>${currentOrder.totalAmount.toFixed(2)}</td>
      <td>
        {currentOrder.deliveryFullname} <br />
        {currentOrder.deliveryAddress} <br />
        {currentOrder.deliveryPostcode}
      </td>
      <td>
        <select
          className="form-select"
          value={currentOrder.deliveryStatus}
          onChange={handleStatusChange}
        >
          <option value="pending">pending</option>
          <option value="shipped">shipped</option>
          <option value="delivered">delivered</option>
          <option value="cancelled">cancelled</option>
        </select>
      </td>
      <td>{currentOrder.paymentMethod}</td>
      <td>
        <div className="d-flex flex-column gap-2">
          {currentOrder.items.map((item, index) => (
            <div key={index}>
              {item.quantity} × {item.product.name}
            </div>
          ))}
        </div>
      </td>
      <td>
        <AdminUpdateButton action={handleSave} />
        <AdminDeleteButton action={handleDelete} />
      </td>
    </tr>
  );
}
