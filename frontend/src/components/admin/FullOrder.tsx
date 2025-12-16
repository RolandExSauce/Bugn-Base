import { useEffect, useRef, useState } from "react";
import type { OrderStatus, Order } from "../../types/models";
import { AdminOrderService } from "../../services";

type FullOrderProps = {
  order: Order;
  onUpdate: (updatedOrder: Order) => void;
  onDelete: (orderId: number) => void;
};

export default function FullOrder({
  order,
  onUpdate,
  onDelete,
}: FullOrderProps) {
  const trRef = useRef<HTMLTableRowElement>(null);
  const [updateOrderStatus, setUpdateOrderStatus] = useState<OrderStatus>(
    order.orderStatus
  );
  const [isEdited, setIsEdited] = useState(false);
  const [isSaving, setIsSaving] = useState(false);

  useEffect(() => {
    setUpdateOrderStatus(order.orderStatus);
  }, [order]);

  const handleStatusChange = (
    e: React.ChangeEvent<HTMLSelectElement>
  ): void => {
    const status = e.target.value as OrderStatus;
    setUpdateOrderStatus(status);
    setIsEdited(status !== order.orderStatus);
  };

  const handleSave = async () => {
    if (!isEdited || isSaving) return;

    setIsSaving(true);
    try {
      // Use the actual order ID, not hardcoded "2"
      const updatedStatus = await AdminOrderService.updateOrderStatus(
        order.id,
        updateOrderStatus
      );

      // Update parent with the new status
      const updatedOrder = {
        ...order,
        orderStatus: updatedStatus,
      };

      // Success animation
      trRef.current?.classList.remove("user-row-success");
      void trRef.current?.offsetWidth;
      trRef.current?.classList.add("user-row-success");

      // Update parent state
      onUpdate(updatedOrder);
      setIsEdited(false);
    } catch (error) {
      console.error("Error updating order status:", error);
      // TODO: Show error message
    } finally {
      setIsSaving(false);
    }
  };

  const handleDelete = async () => {
    if (
      !window.confirm(
        `Möchten Sie Bestellung #${order.orderNumber} wirklich löschen?`
      )
    ) {
      return;
    }

    try {
      await AdminOrderService.deleteOrder(order.id);
      onDelete(order.id);
    } catch (error) {
      console.error("Error deleting order:", error);
      // TODO: Show error message
    }
  };

  // Format date from ISO string
  const formatDate = (dateString: string) => {
    try {
      return new Date(dateString).toLocaleDateString("de-DE", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      });
    } catch {
      return dateString;
    }
  };

  // Get user name safely
  const getUserName = () => {
    if (order.user) {
      return `${order.user.firstname} ${order.user.lastname}`;
    }
    return "Unbekannt";
  };

  return (
    <tr ref={trRef}>
      <td>{order.orderNumber || order.id}</td>
      <td>{getUserName()}</td>
      <td>{formatDate(order.orderedDate)}</td>
      <td>€{order.totalOrderPrice.toFixed(2)}</td>
      <td>{order.shippingAddress}</td>
      <td>
        <select
          className="form-select form-select-sm"
          value={updateOrderStatus}
          onChange={handleStatusChange}
          disabled={isSaving}
        >
          <option value="RECEIVED">Eingegangen</option>
          <option value="PROCESSING">In Bearbeitung</option>
          <option value="SHIPPING">Wird versendet</option>
          <option value="DELIVERED">Geliefert</option>
          <option value="CANCELED">Storniert</option>
          <option value="RETURNED">Retourniert</option>
        </select>
      </td>
      <td>{order.paymentMethod}</td>
      <td>
        <div className="d-flex flex-column gap-2">
          {order.orderItems.map((item, index) => (
            <div key={index}>
              {item.quantity} × {item.product?.name || "Produkt"} (€
              {item.price.toFixed(2)})
            </div>
          ))}
        </div>
      </td>
      <td>
        <div className="d-flex gap-2">
          <button
            className="btn btn-sm btn-success"
            onClick={handleSave}
            disabled={!isEdited || isSaving}
            title="Status speichern"
          >
            {isSaving ? (
              <span
                className="spinner-border spinner-border-sm"
                role="status"
              ></span>
            ) : (
              <i className="bi bi-check"></i>
            )}
          </button>
          <button
            className="btn btn-sm btn-outline-danger"
            onClick={handleDelete}
            title="Bestellung löschen"
          >
            <i className="bi bi-trash"></i>
          </button>
        </div>
      </td>
    </tr>
  );
}



