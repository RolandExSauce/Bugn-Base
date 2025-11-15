import { createContext, useContext, useState } from "react";
import type { CartItem } from "../types/models";

interface CartContextType {
  cart: CartItem[] | undefined;
  addItem: (item: CartItem) => void;
  removeItem: (itemId: string) => void;
  updateItem: (item: CartItem) => void;
  clearCart: () => void;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

// eslint-disable-next-line react-refresh/only-export-components
export const useCartContext = (): CartContextType => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error(
      "useCartContext must be used within a ProfileContextProvider"
    );
  }
  return context;
};

export const CartContextProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const [cart, setCart] = useState<CartItem[] | undefined>(undefined);

  const addItem = (item: CartItem) => {
    setCart([...cart!, item]);
  };

  const removeItem = (itemId: string) => {
    setCart([...cart!.filter((i) => i.product.id !== itemId)]);
  };

  const updateItem = (item: CartItem) => {
    setCart([
      ...cart!.map((i) => (i.product.id === item.product.id ? item : i)),
    ]);
  };

  const clearCart = () => {
    setCart([]);
  };

  const value = {
    cart,
    addItem,
    removeItem,
    clearCart,
    updateItem,
  };

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
};
