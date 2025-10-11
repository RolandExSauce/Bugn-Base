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
  price: number;
  stock: number;
  imageUrl?: string;
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
