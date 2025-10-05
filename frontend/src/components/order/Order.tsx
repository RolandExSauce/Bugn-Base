import { useState } from "react";
import CartItem from "../cart/CartItem";
// import type { Order as OrderDTO } from "../../types/models";

// interface OrderProps {
//   order: OrderDTO;
// }

export function Order() {
  const [showDetails, setShowDetails] = useState(false);

  const order = {
    id: "123",
    user: {
      firstName: "John",
      lastName: "Doe",
    },
    items: [
      {
        id: "123",
        name: "Product 1",
        amount: 1,
        price: 100,
      },
    ],
    createdAt: new Date().toISOString(),
    status: "pending",
  };

  return (
    <div className="border rounded p-3 mb-3">
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-2">
        <div className="d-flex gap-3 flex-wrap">
          <div>
            <strong>Bestellnummer:</strong> {order.id}
          </div>
          <div>
            <strong>Datum:</strong>{" "}
            {new Date(order.createdAt).toLocaleDateString()}
          </div>
          <div>
            <strong>Status:</strong> {order.status}
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
              Lorem street <br /> Ipsum City <br /> Doloria
            </span>
          </div>
          <div className="mt-3 border-top pt-3 d-flex flex-column gap-2 ">
            <span className="fw-bold h5 text--primary">Bestellte Artikel:</span>

            <CartItem editable={false} />
            <CartItem editable={false} />
            <CartItem editable={false} />
          </div>
        </div>
      )}
    </div>
  );
}
