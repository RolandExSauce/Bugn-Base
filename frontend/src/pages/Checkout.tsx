import { useEffect, useRef, useState } from "react";
import type { PaymentMethod, OrderItem, Order } from "../types/models";
import { useNavigate } from "react-router-dom";
import { ADDRESS_REGEX, NAME_REGEX, POSTCODE_REGEX } from "../utils/regex";
import { useCartContext } from "../context/CartContext";
import { useAuthContext } from "../context/AuthContext";
import UserOrderService from "../services/user.order.service";

export default function Checkout() {
  const { auth } = useAuthContext();
  const navigate = useNavigate();
  const divRef = useRef<HTMLDivElement>(null);
  const { clearCart, cart } = useCartContext();

  const [isOrderPlaced, setIsOrderPlaced] = useState(false);
  const [formData, setFormData] = useState({
    deliveryFullname: "",
    deliveryAddress: "",
    deliveryPostcode: 0,
  });
  const [formInvalid, setFormInvalid] = useState({
    deliveryFullname: false,
    deliveryAddress: false,
    deliveryPostcode: false,
  });
  const [paymentMethod, setPaymentMethod] =
    useState<PaymentMethod>("CREDITCARD");

  const handleChange = (key: keyof typeof formData, value: string | number) => {
    setFormData((prev) => ({ ...prev, [key]: value }));
  };

  const handleSubmit = async () => {
    if (!auth || !cart || cart.length === 0) return;

    const nextInvalid = {
      deliveryFullname: !NAME_REGEX.test(formData.deliveryFullname),
      deliveryAddress: !ADDRESS_REGEX.test(formData.deliveryAddress),
      deliveryPostcode: !POSTCODE_REGEX.test(String(formData.deliveryPostcode)),
    };
    setFormInvalid(nextInvalid);

    if (Object.values(nextInvalid).some(Boolean)) return;

    const orderItems = cart.map<OrderItem>((item) => ({
      productId: item.product.id,
      quantity: item.quantity,
      price: item.product.price,
    }));

    const newOrder: Partial<Order> = {
      shippingAddress: formData.deliveryAddress,
      deliveryFullname: formData.deliveryFullname,
      deliveryPostcode: formData.deliveryPostcode,
      paymentMethod,
      orderItems,
    };

    try {
      await UserOrderService.createOrder(newOrder);

      // success animation
      divRef.current?.classList.remove("order-success");
      void divRef.current?.offsetWidth;
      divRef.current?.classList.add("order-success");

      setIsOrderPlaced(true);
      clearCart();
      setFormData({
        deliveryFullname: "",
        deliveryAddress: "",
        deliveryPostcode: 0,
      });
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    if (isOrderPlaced) {
      const timeout = setTimeout(() => navigate("/"), 3000);
      return () => clearTimeout(timeout);
    }
  }, [isOrderPlaced, navigate]);

  if (!auth) return null;

  if (isOrderPlaced) {
    return (
      <div
        ref={divRef}
        className="d-flex h1 text-success justify-content-center align-items-center"
        style={{ height: "80vh" }}
      >
        Bestellung erfolgreich aufgegeben
      </div>
    );
  }

  return (
    <div ref={divRef} className="container mt-4">
      <div className="d-flex flex-column flex-md-row gap-4">
        {/* Delivery form */}
        <div className="flex-fill border rounded p-3">
          <h5 className="mb-3">Lieferadresse</h5>
          <div className="mb-3">
            <label className="form-label">Vollständiger Name</label>
            <input
              type="text"
              className="form-control"
              value={formData.deliveryFullname}
              onChange={(e) => handleChange("deliveryFullname", e.target.value)}
            />
            {formInvalid.deliveryFullname && (
              <div className="text-danger">Name ist ungültig</div>
            )}
          </div>
          <div className="mb-3">
            <label className="form-label">Adresse</label>
            <input
              type="text"
              className="form-control"
              value={formData.deliveryAddress}
              onChange={(e) => handleChange("deliveryAddress", e.target.value)}
            />
            {formInvalid.deliveryAddress && (
              <div className="text-danger">Adresse darf nicht leer sein</div>
            )}
          </div>
          <div className="mb-3">
            <label className="form-label">Postleitzahl</label>
            <input
              type="number"
              className="form-control"
              value={formData.deliveryPostcode}
              onChange={(e) =>
                handleChange("deliveryPostcode", Number(e.target.value))
              }
            />
            {formInvalid.deliveryPostcode && (
              <div className="text-danger">Postleitzahl ist ungültig</div>
            )}
          </div>
        </div>

        {/* Payment & order summary */}
        <div className="flex-fill d-flex flex-column gap-4">
          <div className="border rounded p-3">
            <h5 className="mb-3">Zahlungsmethode</h5>
            <div className="form-check d-flex align-items-center gap-2">
              <input
                className="form-check-input"
                type="radio"
                name="payment"
                id="creditcard"
                checked={paymentMethod === "CREDITCARD"}
                onChange={() => setPaymentMethod("CREDITCARD")}
              />
              <label
                className="form-check-label d-flex align-items-center gap-2"
                htmlFor="creditcard"
              >
                <img src="/card.svg" alt="Kreditkarte" />
                Kreditkarte
              </label>
            </div>
            <div className="form-check d-flex align-items-center gap-2 mt-2">
              <input
                className="form-check-input"
                type="radio"
                name="payment"
                id="paypal"
                checked={paymentMethod === "PAYPAL"}
                onChange={() => setPaymentMethod("PAYPAL")}
              />
              <label
                className="form-check-label d-flex align-items-center gap-2"
                htmlFor="paypal"
              >
                <img width={50} height={50} src="/paypal.svg" alt="PayPal" />
                PayPal
              </label>
            </div>
            <div className="form-check d-flex align-items-center gap-2 mt-2">
              <input
                className="form-check-input"
                type="radio"
                name="payment"
                id="receipt"
                checked={paymentMethod === "BANKTRANSFER"}
                onChange={() => setPaymentMethod("BANKTRANSFER")}
              />
              <label
                className="form-check-label d-flex align-items-center gap-2"
                htmlFor="receipt"
              >
                <img src="/rechnung.svg" alt="Rechnung" />
                Überweisung
              </label>
            </div>
          </div>

          <div className="border rounded p-3">
            <h5 className="mb-3">Bestellübersicht</h5>
            <div className="d-flex flex-column gap-2 mb-3">
              {cart.map((item, i) => (
                <div key={i}>
                  {item.quantity} × {item.product.name}
                </div>
              ))}
            </div>
            <div className="fw-bold border-top pt-2">
              Gesamtbetrag: $
              {cart.reduce(
                (total, item) => total + item.quantity * item.product.price,
                0
              )}
            </div>
            <button
              className="btn btn-success w-100 mt-3"
              onClick={handleSubmit}
            >
              Jetzt kaufen
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
