import { Link } from "react-router-dom";
import { useCartContext } from "../../context/CartContext";
import CartItem from "./CartItem";
import { useEffect, useState } from "react";
import { useAuthContext } from "../../context/AuthContext";

const Cart = () => {
  const { auth } = useAuthContext();
  const { cart } = useCartContext();

  const [totalPrice, setTotalPrice] = useState(0);

  useEffect(() => {
    if (cart === undefined) return;
    let total = 0;
    for (const item of cart) {
      total += item.quantity * item.product.price;
    }
    setTotalPrice(total);
  }, [cart]);

  if (cart === undefined) return;

  return (
    <div className="cart-main container d-flex flex-column row-gap-4 p-3">
      <h1 className="mb-4 text--primary">Warenkorb</h1>
      <div className="cart-listing">
        {cart.map((item, i) => (
          <CartItem item={item} editable={true} key={i} />
        ))}
        {cart.length === 0 && (
          <span className="h4">Der Warenkorb ist leer...</span>
        )}
      </div>
      {cart.length > 0 && (
        <div className="cart-actions w-100 d-flex flex-column row-gap-3 align-items-end">
          <span className="cart-total h3">{totalPrice} €</span>
          {auth ? (
            <Link
              to="/checkout"
              className="bg-success text-white px-4 py-2 fs-4"
            >
              Zur Kasse
            </Link>
          ) : (
            <Link
              to="/auth/login"
              className="bg-success text-white px-4 py-2 fs-4"
            >
              Zur Kasse
            </Link>
          )}
        </div>
      )}
    </div>
  );
};
export default Cart;
