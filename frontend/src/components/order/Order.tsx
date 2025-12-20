import { useState } from "react";
import type { Order } from "../../types/models";
import { UserOrderService } from "../../services";

export function Order({ order }: { order: Order }) {
  const [showDetails, setShowDetails] = useState(false);

  const handleCancelOrder = async () => {
    try {
      UserOrderService.cancelOrder(order.id);
      // TODO: success feedback
    } catch {
      console.log("error canceling the item");
      // todo: handle error
    }
  };

  return (
    <div className="border rounded p-3 mb-3">
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-2">
        <div className="d-flex gap-3 flex-wrap">
          <div>
            <strong>Bestellnummer:</strong> {order.orderNumber}{" "}
            {/* or use some order ID if available */}
          </div>
          <div>
            <strong>An:</strong> {order.deliveryFullname}
          </div>
          <div>
            <strong>Status:</strong>{" "}
            <span className="text--primary fw-bold">{order.orderStatus}</span>
          </div>
        </div>

        <button
          className="admin-details-button"
          onClick={() => setShowDetails(!showDetails)}
        >
          Details
        </button>
      </div>

      {showDetails && (
        <div className="d-flex flex-column row-gap-3 mt-3">
          <div>
            <button
              onClick={handleCancelOrder}
              className="cart-remove-item-button bg-danger text-white px-4 py-2"
            >
              Retournieren
            </button>
          </div>

          <div className="d-flex flex-column row-gap-2">
            <span className="fw-bold border-bottom">Lieferadresse:</span>
            <span>
              {order.shippingAddress} <br />
            </span>
          </div>

          <div className="mt-2">
            <strong>Gesamtbetrag: </strong>
            <span className="text--primary h4">
              {order.totalOrderPrice.toFixed(2)} €
            </span>
          </div>
        </div>
      )}
    </div>
  );
}
