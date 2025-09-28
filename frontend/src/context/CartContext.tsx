import { createContext, useContext, useState } from "react";
import type CartItemDto from "../dto/CartItemDto";

interface CartContextType {
  cart: CartItemDto[] | undefined;
  addItem: (item: CartItemDto) => void;
  removeItem: (item: CartItemDto) => void;
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
  const [cart, setCart] = useState<CartItemDto[] | undefined>(undefined);

  const addItem = (item: CartItemDto) => {
    setCart([...cart!, item]);
  };

  const removeItem = (item: CartItemDto) => {
    setCart([...cart!.filter((i) => i.productId !== item.productId)]);
  };

  const clearCart = () => {
    setCart([]);
  };

  const value = {
    cart,
    addItem,
    removeItem,
    clearCart,
  };

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
};
