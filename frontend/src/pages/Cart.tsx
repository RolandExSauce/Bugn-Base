import { useCartContext } from "../context/CartContext";

export default function Cart() {
  const { cart, addItem, removeItem, clearCart } = useCartContext();

  return <div>Cart</div>;
}
