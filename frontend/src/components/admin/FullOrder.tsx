import { useEffect, useRef, useState } from "react";
import type { OrderStatus, Order, OrderItem } from "../../types/models";
import { AdminOrderService } from "../../services";

type FullOrderProps = {
  order: Order;

  // wie bei Product:
  isSelected: boolean;
  onSelect: () => void;

  onUpdate: (updatedOrder: Order) => void;
  onDelete: (orderId: number) => void;
};

export default function FullOrder({
  order: initialOrder,
  isSelected,
  onSelect,
  onUpdate,
  onDelete,
}: FullOrderProps) {
  const trRef = useRef<HTMLTableRowElement>(null);

  // Lokaler State (wie beim Product)
  const [order, setOrder] = useState<Order>(initialOrder);
  const [updateOrderStatus, setUpdateOrderStatus] = useState<OrderStatus>(
    initialOrder.orderStatus,
  );

  const [isEdited, setIsEdited] = useState(false);
  const [isSaving, setIsSaving] = useState(false);

  useEffect(() => {
    setOrder(initialOrder);
    setUpdateOrderStatus(initialOrder.orderStatus);
    setIsEdited(false);
  }, [initialOrder]);

  const handleStatusChange = (
    e: React.ChangeEvent<HTMLSelectElement>,
  ): void => {
    const status = e.target.value as OrderStatus;
    setUpdateOrderStatus(status);
    setIsEdited(status !== order.orderStatus);
  };

  const handleSave = async () => {
    if (!isEdited || isSaving) return;

    setIsSaving(true);
    try {
      const updatedOrder: Order = { ...order, orderStatus: updateOrderStatus };
      const savedOrder = await AdminOrderService.updateOrder(updatedOrder);

      // Success animation
      trRef.current?.classList.remove("user-row-success");
      void trRef.current?.offsetWidth;
      trRef.current?.classList.add("user-row-success");

      onUpdate(savedOrder);

      // Edit mode verlassen (wie Product)
      setIsEdited(false);
      setTimeout(() => onSelect(), 500);
    } catch (error) {
      console.error("Error updating order status:", error);
    } finally {
      setIsSaving(false);
    }
  };

  const handleCancel = () => {
    setOrder(initialOrder);
    setUpdateOrderStatus(initialOrder.orderStatus);
    setIsEdited(false);
    onSelect(); // zurück in View mode
  };

  const handleDelete = async () => {
    if (
      !window.confirm(
        `Möchten Sie Bestellung #${order.orderNumber || order.id} wirklich löschen?`,
      )
    ) {
      return;
    }

    try {
      await AdminOrderService.deleteOrder(order.id);
      onDelete(order.id);
    } catch (error) {
      console.error("Error deleting order:", error);
    }
  };

  const formatDate = (dateString?: string | null) => {
    if (!dateString) return "-";
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

  const getUserName = () => {
    const first = (order as any).userFirstName?.trim?.() ?? "";
    const last = (order as any).userLastName?.trim?.() ?? "";
    const full = `${first} ${last}`.trim();
    return full.length > 0 ? full : "Unbekannt";
  };

  const statusLabel = (s: OrderStatus) => {
    switch (s) {
      case "RECEIVED":
        return "Eingegangen";
      case "SHIPPING":
        return "Wird versendet";
      case "DELIVERED":
        return "Geliefert";
      case "CANCELED":
        return "Storniert";
      case "RETURNED":
        return "Retourniert";
      default:
        return s;
    }
  };

  // ---------- VIEW MODE (nicht selected) ----------
  if (!isSelected) {
    return (
      <tr>
        <td>{order.orderNumber || order.id}</td>
        <td>{getUserName()}</td>
        <td>{formatDate(order.orderedDate)}</td>
        <td>€{order.totalOrderPrice.toFixed(2)}</td>
        <td>{order.shippingAddress}</td>
        <td>
          {/* nur Anzeige */}
          <span className="badge bg-secondary">
            {statusLabel(order.orderStatus)}
          </span>
        </td>
        <td>{order.paymentMethod}</td>
        <td>
          <div className="d-flex flex-column gap-2">
            {order.orderItems.map((item: OrderItem, index) => (
              <div key={index}>
                {item.productName
                  ? `${item.productName} (x${item.quantity})`
                  : item.productId}
              </div>
            ))}
          </div>
        </td>
        <td>
          <div className="d-flex gap-2">
            <button
              title="Bestellung bearbeiten"
              className="btn btn-sm btn-outline-primary d-flex align-items-center gap-1"
              onClick={onSelect}
              aria-label="Bestellung bearbeiten"
            >
              <img
                src="/update.svg"
                alt="Bearbeiten"
                style={{ width: 14, height: 14 }}
              />
            </button>

            <button
              title="Bestellung löschen"
              className="btn btn-sm btn-outline-danger d-flex align-items-center gap-1"
              onClick={handleDelete}
              aria-label="Bestellung löschen"
            >
              <img
                src="/delete.svg"
                alt="Löschen"
                style={{ width: 14, height: 14 }}
              />
            </button>
          </div>
        </td>
      </tr>
    );
  }

  // ---------- EDIT MODE (selected) ----------
  return (
    <tr ref={trRef} className="table-warning">
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
          <option value="SHIPPING">Wird versendet</option>
          <option value="DELIVERED">Geliefert</option>
          <option value="CANCELED">Storniert</option>
          <option value="RETURNED">Retourniert</option>
        </select>
      </td>

      <td>{order.paymentMethod}</td>

      <td>
        <div className="d-flex flex-column gap-2">
          {order.orderItems.map((item: OrderItem, index) => (
            <div key={index}>
              {item.productName
                ? `${item.productName} (x${item.quantity})`
                : item.productId}
            </div>
          ))}
        </div>
      </td>

      <td>
        <div className="d-flex gap-2">
          <button
            title="Speichern"
            className="btn btn-sm btn-success d-flex align-items-center gap-1"
            onClick={handleSave}
            disabled={!isEdited || isSaving}
            aria-label="Speichern"
          >
            <img
              src="/save.svg"
              alt="Speichern"
              style={{ width: 16, height: 16 }}
            />
          </button>

          <button
            title="Abbrechen"
            className="btn btn-sm btn-secondary d-flex align-items-center gap-1"
            onClick={handleCancel}
            disabled={isSaving}
            aria-label="Abbrechen"
          >
            <img
              src="/undo.svg"
              alt="Abbrechen"
              style={{ width: 16, height: 16 }}
            />
          </button>
        </div>
      </td>
    </tr>
  );
}
