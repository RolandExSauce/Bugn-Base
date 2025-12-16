import { useEffect, useState } from "react";
import type { Order } from "../../types/models";
import FullOrder from "../../components/admin/FullOrder";
import { AdminOrderService } from "../../services";

export default function OrdersList() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const data = await AdminOrderService.getAllOrders();
      setOrders(data);
      setError(null);
    } catch (err) {
      setError("Bestellungen konnten nicht geladen werden.");
      console.error("Error fetching orders:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const handleOrderUpdated = (updatedOrder: Order) => {
    setOrders((prev) =>
      prev.map((order) => (order.id === updatedOrder.id ? updatedOrder : order))
    );
  };

  const handleOrderDeleted = (orderId: number) => {
    setOrders((prev) => prev.filter((order) => order.id !== orderId));
  };

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 ms-3">Lade Bestellungen...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-danger">
        {error}
        <button
          onClick={fetchOrders}
          className="btn btn-sm btn-outline-danger ms-3"
        >
          Erneut versuchen
        </button>
      </div>
    );
  }

  if (orders.length === 0) {
    return (
      <div className="alert alert-info">Keine Bestellungen vorhanden.</div>
    );
  }

  return (
    <div className="table-responsive">
      <table className="table table-bordered table-striped">
        <thead>
          <tr>
            <th>Bestellnummer</th>
            <th>Kunde</th>
            <th>Datum</th>
            <th>Gesamtbetrag</th>
            <th>Lieferadresse</th>
            <th>Status</th>
            <th>Zahlungsmethode</th>
            <th>Bestellte Artikel</th>
            <th>Aktionen</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order) => (
            <FullOrder
              key={order.id}
              order={order}
              onUpdate={handleOrderUpdated}
              onDelete={handleOrderDeleted}
            />
          ))}
        </tbody>
      </table>
    </div>
  );
}
