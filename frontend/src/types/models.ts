// == PRODUCTS TYPES AND RELATED =========================================================================================================
export type ProductCategory = "PIANOS" | "GUITARS" | "VIOLINS";
export type SortType = "price-asc" | "price-desc" | "";
export type StockStatus = "IN_STOCK" | "OUT_OF_STOCK" | "LOW_STOCK";
export type OrderStatus =
  | "CANCELED"
  | "RECEIVED"
  | "SHIPPING"
  | "DELIVERED"
  | "RETURNED";

export interface Product {
  id: number;
  name: string;
  category: ProductCategory;
  description: string;
  price: number;
  shippingCost: number;
  brand: string;
  stockStatus: StockStatus;
  shippingTime: number;
  active: boolean;
  images: Image[];
}

// For adding/updating products
export interface ProductDTO {
  name: string;
  category: ProductCategory;
  description: string;
  price: number;
  shippingCost: number;
  brand: string;
  stockStatus: StockStatus;
  shippingTime: number;
  active: boolean;
}

export interface ProductFilter {
  name?: string;
  category?: ProductCategory;
  priceMin?: number;
  priceMax?: number;
  brand?: string[];
  pageNumber?: number;
  pageSize?: number;
  sort?: SortType; // Frontend-only: for client-side sorting
  stars?: number; // Frontend-only: for client-side star filtering
}

export interface Image {
  imageId: string;
  url: string;
  product?: Product;
  altText?: string;
}

// == USER TYPES AND RELATED =========================================================================================================
export type Role = "ROLE_USER" | "ROLE_ADMIN";
export type PaymentMethod = "CREDITCARD" | "PAYPAL" | "BANKTRANSFER";

export interface User {
  id: string;
  firstname: string;
  lastname: string;
  phone?: string | number;
  address?: string;
  postcode: number;
  email: string;
  active: boolean;
  createdAt: Date;
  role: Role;
}

export interface AuthState {
  user: User;
  accessToken: string;
  role: Role;
}

export interface LoginDto {
  email: string;
  password: string;
}

export interface RegisterDto {
  firstname: string;
  lastname: string;
  email: string;
  password: string;
}

// == ORDER TYPES AND RELATED =========================================================================================================
// For making orders
export interface OrderDTO {
  customerEmail: string;
  customerPhoneNumber: string;
  shippingStateAndDistrict: string;
  shippingAddress: string;
  totalOrderPrice: number;
  orderItems: OrderItemDTO[];
  status?: OrderStatus;
}

export interface OrderItemDTO {
  productId: string;
  quantity: number;
  price: number;
}

// Backend Order Model (for responses)
export interface Order {
  id: number;
  orderNumber: string;
  user: User;
  totalOrderPrice: number;
  orderItems: OrderItem[];
  orderedDate: string;
  deliveryDate: string;
  orderStatus: OrderStatus;
  shippingAddress: string;
  paymentMethod: PaymentMethod;
}

export interface OrderItem {
  id: number;
  order: Order;
  product: Product;
  quantity: number;
  price: number;
}

// == CART TYPES AND RELATED =========================================================================================================
export interface CartItemType {
  product: Product;
  quantity: number;
}

export interface Cart {
  items: CartItemType[];
  totalPrice: number;
}

// == REVIEW AND MESSAGES TYPES AND RELATED =========================================================================================================
export interface Review {
  reviewId: string;
  product: Product;
  username: string;
  rating: number;
  comment: string;
  createdAt: Date;
}

export interface MessageDto {
  messageId: string;
  name: string;
  email: string;
  subject: string;
  message: string;
  createdAt: Date;
}
