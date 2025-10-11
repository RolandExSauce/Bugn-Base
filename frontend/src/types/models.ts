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
  orderDate: Date;
  totalAmount: number;
  deliveryFullname: string;
  deliveryAddress: string;
  deliveryPostcode: number;
  paymentMethod: PaymentMethod;
  items: OrderItem[];
  deliveryStatus: DeliveryStatus;
}

export type ItemCategory = "piano" | "guitar" | "violin";

export type PaymentMethod = "creditcard" | "transfer";

export type DeliveryStatus = "pending" | "shipped" | "delivered" | "cancelled";

type OrderItem = { product: Product; quantity: number };
