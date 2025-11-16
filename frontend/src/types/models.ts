// User
export interface User {
  id: string;
  firstname: string;
  lastname: string;
  phone: number | undefined;
  address: string | undefined;
  postcode: number | undefined;
  email: string;
  active: boolean;
  createdAt: Date;
  role: Role;
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

// Auth
export interface AuthState {
  user: User;
  accessToken: string;
  role: string;
}

export interface Login {
  email: string;
  password: string;
}

export interface Register {
  firstname: string;
  lastname: string;
  email: string;
  password: string;
}

// Cart
export interface CartItemType {
  product: Product;
  quantity: number;
}

export interface Cart {
  items: CartItemType[];
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
  items: CartItemType[];
  deliveryStatus: DeliveryStatus;
}

// Review Dto
export interface Review {
  reviewId: string;
  product: Product;
  username: string;
  rating: number;
  comment: string;
  createdAt: Date;
}

// MEssageDto

export interface MessageDto {
  messageId: string;
  name: string;
  email: string;
  subject: string;
  message: string;
  createdAt: Date;
}

export interface FilterDto {
  category: ItemCategory;
  brands: string[];
  sort: SortType;
  stars: number | undefined;
}

export type ItemCategory = "piano" | "guitar" | "violin";

export type PaymentMethod = "creditcard" | "paypal" | "receipt";

export type DeliveryStatus = "pending" | "shipped" | "delivered" | "cancelled";

export type Role = "user" | "admin";

export type SortType = "price-asc" | "price-desc" | "";
