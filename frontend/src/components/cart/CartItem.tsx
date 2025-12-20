import type { CartItemType } from "../../types/models";
import { useCartContext } from "../../context/CartContext";

const CartItem = ({
  editable,
  item,
}: {
  editable: boolean;
  item: CartItemType;
}) => {
  const { removeItem } = useCartContext();

  return (
    <div className="d-flex align-items-center justify-content-between border rounded p-3 mb-3 flex-md-row flex-column">
      <div className="d-flex align-items-center column-gap-3">
        <img
          src={`${import.meta.env.VITE_BASE_URL}/media${
            item.product.images[0].url
          }`}
          alt="Product 1"
          width="50"
          height="50"
          className="rounded"
        />
        <span
          className="cart-item-name fs-5 text-truncate"
          style={{ width: "250px" }}
        >
          {item.product.name}
        </span>
      </div>
      <div className="d-flex flex-column align-items-center">
        <div className="text-secondary">Menge</div>
        <div>{item.quantity}</div>
      </div>
      <div className="d-flex flex-column align-items-center">
        <div className="text-secondary">Einzelpreis</div>
        <div>{item.product.price} €</div>
      </div>
      <div className="d-flex flex-column align-items-center">
        <div className="text-secondary">Gesamt</div>
        <div>{item.product.price * item.quantity} €</div>
      </div>
      {editable && (
        <button
          onClick={() => removeItem(item.product.id)}
          className="cart-remove-item-button bg-danger text-white px-4 py-2"
        >
          Entfernen
        </button>
      )}
    </div>
  );
};
export default CartItem;
