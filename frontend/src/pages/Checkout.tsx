import { useEffect, useState } from "react";
import { mockOrder } from "../types/temp/PlaceholderData";
import type { PaymentMethod } from "../types/models";
import { useNavigate } from "react-router-dom";

export default function Checkout() {
  const navigate = useNavigate();

  const [isOrderPlaced, setIsOrderPlaced] = useState(false);

  const [paymentMethod, setPaymentMethod] =
    useState<PaymentMethod>("creditcard");
  const order = mockOrder;

  useEffect(() => {
    if (isOrderPlaced) {
      setTimeout(() => {
        navigate("/");
      }, 3000);
    }
  }, [isOrderPlaced]);

  if (isOrderPlaced) {
    return (
      <div
        className="d-flex h1 text-success justify-content-center align-items-center"
        style={{ height: "80vh" }}
      >
        Bestellung aufgegeben
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <div className="d-flex flex-column flex-md-row gap-4">
        <div className="flex-fill border rounded p-3">
          <h5 className="mb-3">Lieferadresse</h5>
          <div className="mb-3">
            <label className="form-label">Vollständiger Name</label>
            <input
              type="text"
              className="form-control"
              defaultValue={order.deliveryFullname}
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Adresse</label>
            <input
              type="text"
              className="form-control"
              defaultValue={order.deliveryAddress}
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Postleitzahl</label>
            <input
              type="number"
              className="form-control"
              defaultValue={order.deliveryPostcode}
            />
          </div>
        </div>

        <div className="flex-fill d-flex flex-column gap-4">
          <div className="border rounded p-3">
            <h5 className="mb-3">Zahlungsmethode</h5>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="payment"
                id="creditcard"
                checked={paymentMethod === "creditcard"}
                onChange={() => setPaymentMethod("creditcard")}
              />
              <label className="form-check-label" htmlFor="creditcard">
                Kreditkarte
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="payment"
                id="paypal"
                checked={paymentMethod === "paypal"}
                onChange={() => setPaymentMethod("paypal")}
              />
              <label className="form-check-label" htmlFor="paypal">
                PayPal
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="payment"
                id="receipt"
                checked={paymentMethod === "receipt"}
                onChange={() => setPaymentMethod("receipt")}
              />
              <label className="form-check-label" htmlFor="receipt">
                Rechnung
              </label>
            </div>
          </div>

          <div className="border rounded p-3">
            <h5 className="mb-3">Bestellübersicht</h5>
            <div className="d-flex flex-column gap-2 mb-3">
              {order.items.map((item, index) => (
                <div key={index}>
                  {item.quantity} × {item.product.name}
                </div>
              ))}
            </div>
            <div className="fw-bold border-top pt-2">
              Gesamt: ${order.totalAmount.toFixed(2)}
            </div>
            <button
              onClick={() => setIsOrderPlaced(true)}
              className="btn btn-success w-100 mt-3"
            >
              Jetzt kaufen
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
