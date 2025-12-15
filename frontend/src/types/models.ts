export type ProductCategory = "PIANO" | "GUITAR" | "VIOLIN";
export type StockStatus = "IN_STOCK" | "OUT_OF_STOCK" | "LOW_STOCK";
export type OrderStatus = "RECEIVED" | "PROCESSING" | "SHIPPING" | "DELIVERED" | "CANCELED" | "RETURNED";
export type PaymentMethod = "CREDIT_CARD" | "PAYPAL" | "RECEIPT";
export type Role = "ROLE_USER" | "ROLE_ADMIN";

// Order Types
export interface OrderDTO {
  customerEmail: string;
  customerPhoneNumber: string;
  shippingStateAndDistrict: string;
  shippingAddress: string;
  totalOrderPrice: number;
  orderItems: OrderItemDTO[];
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
  orderedDate: string; // LocalDate from backend
  deliveryDate: string; // LocalDate from backend
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

// Product Types
export interface Product {
  id: number; // Changed from string to number (Long in backend)
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

export interface ProductDTO {
  name: string;
  category: string; // String in DTO, enum in model
  description: string;
  price: number;
  shippingCost: number;
  brand: string;
  stockStatus: StockStatus;
  shippingTime: number;
  active: boolean;
}

export interface FilterDto {
  category: ProductCategory;
  brands: string[];
  sort: SortType;
  stars?: number;
}

export type SortType = "price-asc" | "price-desc" | "";

export interface Image {
  imageId: string; // UUID in backend
  url: string;
  product?: Product; // Optional back reference
  altText?: string; 
}

// User Types
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

// Auth Types
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

// Filter Types
export interface ProductFilter {
  name?: string;
  category?: ProductCategory;
  priceMin?: number;
  priceMax?: number;
  brand?: string[];
  pageNumber?: number; // Changed from pageNo to pageNumber
  pageSize?: number;   // Changed from pageSize to match backend
}

// Cart Types
export interface CartItemType {
  product: Product;
  quantity: number;
}

export interface Cart {
  items: CartItemType[];
  totalPrice: number;
}

// Review Types
export interface Review {
  reviewId: string;
  product: Product;
  username: string;
  rating: number;
  comment: string;
  createdAt: Date;
}

// Message Types
export interface MessageDto {
  messageId: string;
  name: string;
  email: string;
  subject: string;
  message: string;
  createdAt: Date;
}