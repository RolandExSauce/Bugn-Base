import { useState } from "react";
import CartItem from "../cart/CartItem";
import type { Order } from "../../types/models";

export function Order({ order }: { order: Order }) {
  const [showDetails, setShowDetails] = useState(false);

  return (
    <div className="border rounded p-3 mb-3">
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-2">
        <div className="d-flex gap-3 flex-wrap">
          <div>
            <strong>Bestellnummer:</strong> {order.id}
          </div>
          <div>
            <strong>Datum:</strong>{" "}
            {new Date(order.orderDate).toLocaleDateString()}
          </div>
          <div>
            <strong>Status:</strong> {order.deliveryStatus}
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
            <button className="cart-remove-item-button bg-danger text-white px-4 py-2">
              Retournieren
            </button>
          </div>

          <div className="d-flex flex-column row-gap-2">
            <span className="fw-bold border-bottom">Lieferadresse:</span>
            <span>
              {order.deliveryFullname} <br />
              {order.deliveryAddress} <br />
              {order.deliveryPostcode}
            </span>
          </div>

          <div className="d-flex flex-column row-gap-2">
            <span className="fw-bold border-bottom">Zahlungsmethode:</span>

            <span className="d-flex align-items-center gap-2">
              {order.paymentMethod === "creditcard" && (
                <>
                  <img src="/card.svg" alt="Kreditkarte" />
                  Kreditkarte
                </>
              )}

              {order.paymentMethod === "paypal" && (
                <>
                  <img src="/paypal.svg" alt="PayPal" />
                  PayPal
                </>
              )}

              {order.paymentMethod === "receipt" && (
                <>
                  <img src="/rechnung.svg" alt="Rechnung" />
                  Rechnung
                </>
              )}
            </span>
          </div>

          <div className="mt-3 border-top pt-3 d-flex flex-column gap-2">
            <span className="fw-bold h5 text--primary">Bestellte Artikel:</span>
            {Array.from({ length: 2 }).map((_, i) => (
              <CartItem item={order.items[i]} editable={false} key={i} />
            ))}
          </div>

          <div className="mt-2">
            <strong>Gesamtbetrag: </strong>
            <span className="text--primary h4">
              {order.totalAmount.toFixed(2)} €
            </span>
          </div>
        </div>
      )}
    </div>
  );
}
