import CartItem from "./CartItem";

const Cart = () => {
  return (
    <div className="cart-main d-flex flex-column row-gap-4 p-3">
      <span className="cart-title fs-1">Warenkorb</span>
      <div className="cart-listing">
        {Array.from({ length: 3 }).map((_, i) => (
          <CartItem key={i} />
        ))}
      </div>
      <div className="cart-actions w-100 d-flex flex-column row-gap-3 align-items-end">
        <span className="cart-total h3">Gesamt: 3000€</span>
        <button className="bg-success text-white px-4 py-2 fs-4">
          Zur Kasse
        </button>
      </div>
    </div>
  );
};
export default Cart;
