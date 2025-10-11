// User
export interface User {
  id: string;
  firstname: string;
  lastname: string;
  phone: string;
  address: string;
  postcode: number;
  email: string;
  active: boolean;
  createdAt: Date;
  role: "USER" | "ADMIN";
}

// Product
export interface Product {
  id: string;
  name: string;
  category: ItemCategory;
  description: string;
  price: number;
  shippingCost: number;
  brand: string;
  stockStatus: boolean;
  shippingTime: number;
  active: boolean;
}

// Cart
export interface CartItem {
  id: string;
  product: Product;
  quantity: number;
}

export interface Cart {
  items: CartItem[];
  totalPrice: number;
}

// Order
export interface Order {
  id: string;
  user: User;
  items: CartItem[];
  status: "pending" | "shipped" | "delivered" | "cancelled";
  createdAt: string;
}

export type ItemCategory = "piano" | "guitar" | "violin";
