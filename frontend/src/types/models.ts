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

export type ProductFilter = {
  name?: string | null;
  category?: ProductCategory | null;
  priceMin?: number | null;
  priceMax?: number | null;
  brand?: string[] | null;
  pageNumber?: number | null;
  pageSize?: number | null;
  sort?: SortType;
  stars?: number;
};



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

export interface Order {
  id: number;
  orderNumber: string;

  userFirstName: string;
  userLastName: string;

  totalOrderPrice: number;
  orderItems: OrderItem[];
  orderedDate: string;
  deliveryDate: string;
  orderStatus: OrderStatus;
  shippingAddress: string;
  paymentMethod: PaymentMethod;
  deliveryPostcode: number;
}


export interface OrderItem {
  productId: number;
  productName: string;
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

export type MessageStatus = "OPEN" | "ANSWERED";

export interface MessageDto {
  id: string;
  name: string;
  email: string;
  subject: string | null;
  message: string;
  createdAt: string;
  adminReply: string | null;
  repliedAt: string | null;
  messageStatus: MessageStatus;
  readAt: string | null;          
}

export type Review = {
  id: string;
  userName: string;
  rating: number; // 1..5
  comment?: string | null;
  createdAt: string; // ISO
};

export type CreateReviewRequest = {
  productId: number; // oder string, je nachdem wie du Product.id typisiert hast
  rating: number;
  comment?: string;
};

