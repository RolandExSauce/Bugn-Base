import { useEffect, useState } from "react";
import type { Order } from "../../types/models";
import UserOrderService from "../../services/user.order.service";
import CreateReviewForm from "../../pages/user/CreateReviewForm";

export function Order({ order }: { order: Order }) {
  const [showDetails, setShowDetails] = useState(false);

  const [localStatus, setLocalStatus] = useState(order.orderStatus);
  const [reviewProductId, setReviewProductId] = useState<number | null>(null);

  const [updating, setUpdating] = useState(false);
  const [actionError, setActionError] = useState<string | null>(null);

  useEffect(() => {
    setLocalStatus(order.orderStatus);
  }, [order.orderStatus]);

  const isReceived = localStatus === "RECEIVED";
  const isDelivered = localStatus === "DELIVERED";
  const isShipping = localStatus === "SHIPPING";

  const getUserName = () => {
    const first = order.userFirstName?.trim() ?? "";
    const last = order.userLastName?.trim() ?? "";
    const full = `${first} ${last}`.trim();
    return full || "Unbekannt";
  };

  const handleCancelOrder = async () => {
    try {
      setUpdating(true);
      setActionError(null);

      const newStatus = await UserOrderService.cancelOrder(order.id);
      setLocalStatus(newStatus);
      setReviewProductId(null);
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.response?.data ??
        err?.message ??
        "Fehler beim Stornieren";
      setActionError(String(msg));
    } finally {
      setUpdating(false);
    }
  };

  const handleReturnOrder = async () => {
    try {
      setUpdating(true);
      setActionError(null);

      const newStatus = await UserOrderService.returnOrder(order.id);
      setLocalStatus(newStatus);
      setReviewProductId(null);
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.response?.data ??
        err?.message ??
        "Fehler beim Retournieren";
      setActionError(String(msg));
    } finally {
      setUpdating(false);
    }
  };

  const getProductId = (item: any): number | null => {
    if (typeof item?.productId === "number") return item.productId;
    if (typeof item?.product?.id === "number") return item.product.id;
    return null;
  };

  const getProductName = (item: any): string => {
    return (
      item?.productName ??
      item?.product?.name ??
      `Produkt #${getProductId(item) ?? ""}`.trim()
    );
  };

  return (
    <div className="border rounded p-3 mb-3">
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-2">
        <div className="d-flex gap-3 flex-wrap">
          <div>
            <strong>Bestellnummer:</strong> {order.orderNumber}
          </div>
          <div>
            <strong>An:</strong> {getUserName()}
          </div>
          <div>
            <strong>Status:</strong>{" "}
            <span className="text--primary fw-bold">{localStatus}</span>
          </div>
        </div>

        <button
          className="admin-details-button"
          type="button"
          onClick={() => {
            setShowDetails((v) => !v);
            if (showDetails) setReviewProductId(null);
          }}
        >
          Details
        </button>
      </div>

      {showDetails && (
        <div className="d-flex flex-column row-gap-3 mt-3">
          <div className="d-flex gap-2 flex-wrap align-items-center">
            {isReceived && (
              <button
                onClick={handleCancelOrder}
                className="cart-remove-item-button bg-danger text-white px-4 py-2"
                type="button"
                disabled={updating}
              >
                {updating ? "..." : "Stornieren"}
              </button>
            )}

            {isDelivered && (
              <button
                onClick={handleReturnOrder}
                className="cart-remove-item-button bg-danger text-white px-4 py-2"
                type="button"
                disabled={updating}
              >
                {updating ? "..." : "Retournieren"}
              </button>
            )}

            {isShipping && (
              <span className="text-muted fst-italic">
                Bestellung ist im Versand – keine Aktion möglich.
              </span>
            )}

            {actionError && <span className="text-danger">{actionError}</span>}
          </div>

          <div className="d-flex flex-column row-gap-2">
            <span className="fw-bold border-bottom">Produkte:</span>

            {Array.isArray((order as any).orderItems) &&
            (order as any).orderItems.length > 0 ? (
              (order as any).orderItems.map((item: any, idx: number) => {
                const productId = getProductId(item);
                if (!productId) return null;

                const open = reviewProductId === productId;

                return (
                  <div key={idx} className="d-flex flex-column gap-2">
                    <div className="d-flex justify-content-between align-items-center">
                      <div className="small">{getProductName(item)}</div>

                      {isDelivered && (
                        <button
                          className="btn btn-sm btn-outline-primary"
                          type="button"
                          disabled={updating}
                          onClick={() =>
                            setReviewProductId(open ? null : productId)
                          }
                        >
                          {open ? "Schließen" : "Bewerten"}
                        </button>
                      )}
                    </div>

                    {isDelivered && open && (
                      <CreateReviewForm
                        productId={productId}
                        onCancel={() => setReviewProductId(null)}
                        onCreated={() => {
                          setReviewProductId(null);
                        }}
                      />
                    )}
                  </div>
                );
              })
            ) : (
              <span className="text-muted fst-italic">
                Keine Produkte gefunden.
              </span>
            )}
          </div>

          <div className="d-flex flex-column row-gap-2">
            <span className="fw-bold border-bottom">Lieferadresse:</span>
            <span>{order.shippingAddress}</span>
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
