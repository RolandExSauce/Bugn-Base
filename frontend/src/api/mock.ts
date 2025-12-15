import type { MessageDto, Product, User, Order, OrderItem, CartItemType, Image, OrderStatus, PaymentMethod, StockStatus, ProductCategory } from "../types/models";

// Mock Images
const mockImage: Image = {
  imageId: "img1",
  url: "https://example.com/product1.jpg",
  altText: "Product image"
};

export const mockMessage1: MessageDto = {
  messageId: "m1",
  name: "Alice Johnson",
  email: "alice.johnson@example.com",
  subject: "Inquiry about product",
  message:
    "Hi, I wanted to ask if the PrestoTune White-F is available in stock?",
  createdAt: new Date("2025-11-10T10:30:00"),
};

export const mockMessage2: MessageDto = {
  messageId: "m2",
  name: "Bob Smith",
  email: "bob.smith@example.com",
  subject: "Issue with order #1234",
  message: "I received my order but one item was missing. Can you assist?",
  createdAt: new Date("2025-11-11T14:15:00"),
};

export const mockMessage3: MessageDto = {
  messageId: "m3",
  name: "Clara Lee",
  email: "clara.lee@example.com",
  subject: "Feedback on service",
  message: "Great service! The piano arrived in perfect condition. Thank you!",
  createdAt: new Date("2025-11-12T09:45:00"),
};

export const mockMessage4: MessageDto = {
  messageId: "m4",
  name: "Daniel Kim",
  email: "daniel.kim@example.com",
  subject: "Request for invoice",
  message: "Could you send me the invoice for my last purchase?",
  createdAt: new Date("2025-11-12T16:20:00"),
};

export const mockUser: User = {
  id: "1",
  firstname: "Alice",
  lastname: "Smith",
  phone: "1234567890",
  address: "Street 1",
  postcode: 1000,
  email: "alice@mail.com",
  active: true,
  createdAt: new Date(),
  role: "ROLE_ADMIN",
};

export const mockUser2: User = {
  id: "2",
  firstname: "Emmy",
  lastname: "Smith",
  phone: "1234567890",
  address: "Street 1",
  postcode: 1000,
  email: "emmy@mail.com",
  active: true,
  createdAt: new Date(),
  role: "ROLE_USER",
};

export const mockUser3: User = {
  id: "3",
  firstname: "Otto",
  lastname: "Smith",
  phone: "1234567890",
  address: "Street 1",
  postcode: 1000,
  email: "otto@mail.com",
  active: true,
  createdAt: new Date(),
  role: "ROLE_USER",
};

// Base mock product
const baseMockProduct: Product = {
  id: 1,
  name: "Placeholder product name",
  category: "GUITAR" as ProductCategory,
  description: `Lorem ipsum dolor sit, amet consectetur adipisicing elit. Enim
    dolor, doloribus fuga temporibus aspernatur odio rerum error labore
    adipisci magni pariatur ex facilis excepturi dicta consectetur r sit
    amet consectetur adipisicing elit. Sequi ipsa quis nam quas, veniam
    sapiente, nesciunt asperiores maxime odit, vitae adipisci quisquam
    cupiditate tenetur officiis error inventore? Dolorem, sit illum?`,
  price: 499.99,
  shippingCost: 15.0,
  brand: "New Brand",
  stockStatus: "IN_STOCK" as StockStatus,
  shippingTime: 3,
  active: true,
  images: [mockImage]
};

// Mock cart items for order
export const mockCartItems: CartItemType[] = [
  { product: baseMockProduct, quantity: 2 },
  { product: baseMockProduct, quantity: 1 },
];

// Mock order items for order
const mockOrderItems: OrderItem[] = [
  {
    id: 1,
    order: {} as Order, // Will be set below
    product: baseMockProduct,
    quantity: 2,
    price: baseMockProduct.price
  },
  {
    id: 2,
    order: {} as Order, // Will be set below
    product: baseMockProduct,
    quantity: 1,
    price: baseMockProduct.price
  }
];

export const mockOrder: Order = {
  id: 1001,
  orderNumber: "ORD1001",
  user: mockUser2,
  orderItems: mockOrderItems,
  orderedDate: "2025-10-11",
  deliveryDate: "2025-10-25",
  orderStatus: "PENDING" as OrderStatus, // Note: Changed from "pending" to "PENDING"
  shippingAddress: "123 Main Street, New York, NY 10001",
  totalOrderPrice: baseMockProduct.price * 3 + baseMockProduct.shippingCost * 3,
  paymentMethod: "CREDIT_CARD" as PaymentMethod,
};

// Set the order reference in order items
mockOrderItems.forEach(item => item.order = mockOrder);

export const mockProducts: Product[] = [
  // PIANO
  {
    id: 1,
    name: "PrestoTone Mini",
    category: "PIANO" as ProductCategory,
    description: "Compact digital piano with weighted keys and simple controls.",
    price: 450.00,
    shippingCost: 0,
    brand: "PrestoTune",
    stockStatus: "IN_STOCK" as StockStatus,
    shippingTime: 3,
    active: true,
    images: [{
      imageId: "img_p1",
      url: "https://example.com/piano1.jpg",
      altText: "PrestoTone Mini Piano"
    }]
  },
  {
    id: 2,
    name: "StrumVista Compact",
    category: "PIANO" as ProductCategory,
    description: "Entry-level upright piano with balanced tone.",
    price: 780.00,
    shippingCost: 12.00,
    brand: "StrumVista",
    stockStatus: "IN_STOCK" as StockStatus,
    shippingTime: 5,
    active: true,
    images: [{
      imageId: "img_p2",
      url: "https://example.com/piano2.jpg",
      altText: "StrumVista Compact Piano"
    }]
  },
  {
    id: 3,
    name: "EchoLynx Studio 44",
    category: "PIANO" as ProductCategory,
    description: "Studio-focused piano with enhanced resonance system.",
    price: 1100.00,
    shippingCost: 15.00,
    brand: "EchoLynx",
    stockStatus: "OUT_OF_STOCK" as StockStatus,
    shippingTime: 6,
    active: false,
    images: [{
      imageId: "img_p3",
      url: "https://example.com/piano3.jpg",
      altText: "EchoLynx Studio 44"
    }]
  },
  {
    id: 4,
    name: "SonataCraft Micro Grand",
    category: "PIANO" as ProductCategory,
    description: "Small-form grand piano with concert-style dynamics.",
    price: 3100.00,
    shippingCost: 30.00,
    brand: "SonataCraft",
    stockStatus: "LOW_STOCK" as StockStatus,
    shippingTime: 4,
    active: true,
    images: [{
      imageId: "img_p4",
      url: "https://example.com/piano4.jpg",
      altText: "SonataCraft Micro Grand"
    }]
  },

  // GUITAR
  {
    id: 5,
    name: "ToneRiver Classic",
    category: "GUITAR" as ProductCategory,
    description: "Traditional acoustic guitar with warm midrange.",
    price: 590.00,
    shippingCost: 9.00,
    brand: "ToneRiver",
    stockStatus: "IN_STOCK" as StockStatus,
    shippingTime: 5,
    active: true,
    images: [{
      imageId: "img_g1",
      url: "https://example.com/guitar1.jpg",
      altText: "ToneRiver Classic Guitar"
    }]
  },
  {
    id: 6,
    name: "FretHarmony Edge",
    category: "GUITAR" as ProductCategory,
    description: "Electric guitar with modern pickup configuration.",
    price: 990.00,
    shippingCost: 15.00,
    brand: "FretHarmony",
    stockStatus: "IN_STOCK" as StockStatus,
    shippingTime: 2,
    active: true,
    images: [{
      imageId: "img_g2",
      url: "https://example.com/guitar2.jpg",
      altText: "FretHarmony Edge Guitar"
    }]
  },
  {
    id: 7,
    name: "EchoRidge Touring",
    category: "GUITAR" as ProductCategory,
    description: "Tour-ready guitar with reinforced body and clean output.",
    price: 1450.00,
    shippingCost: 21.00,
    brand: "EchoRidge",
    stockStatus: "OUT_OF_STOCK" as StockStatus,
    shippingTime: 8,
    active: false,
    images: [{
      imageId: "img_g3",
      url: "https://example.com/guitar3.jpg",
      altText: "EchoRidge Touring Guitar"
    }]
  },
  {
    id: 8,
    name: "StringSpirit Solo",
    category: "GUITAR" as ProductCategory,
    description: "Lightweight guitar designed for flexible solo performance.",
    price: 720.00,
    shippingCost: 7.00,
    brand: "StringSpirit",
    stockStatus: "IN_STOCK" as StockStatus,
    shippingTime: 3,
    active: true,
    images: [{
      imageId: "img_g4",
      url: "https://example.com/guitar4.jpg",
      altText: "StringSpirit Solo Guitar"
    }]
  },

  // VIOLIN
  {
    id: 9,
    name: "ClassicKeys Mini Violin",
    category: "VIOLIN" as ProductCategory,
    description: "Compact violin with clean projection and durable build.",
    price: 550.00,
    shippingCost: 8.00,
    brand: "ClassicKeys",
    stockStatus: "IN_STOCK" as StockStatus,
    shippingTime: 4,
    active: true,
    images: [{
      imageId: "img_v1",
      url: "https://example.com/violin1.jpg",
      altText: "ClassicKeys Mini Violin"
    }]
  },
  {
    id: 10,
    name: "Virtuoso Nova",
    category: "VIOLIN" as ProductCategory,
    description: "Hand-carved violin with warm upper-register clarity.",
    price: 880.00,
    shippingCost: 12.00,
    brand: "Virtuoso",
    stockStatus: "IN_STOCK" as StockStatus,
    shippingTime: 2,
    active: true,
    images: [{
      imageId: "img_v2",
      url: "https://example.com/violin2.jpg",
      altText: "Virtuoso Nova Violin"
    }]
  },
  {
    id: 11,
    name: "FretHarmony Arco",
    category: "VIOLIN" as ProductCategory,
    description: "Violin with balanced overtones and responsive bow handling.",
    price: 970.00,
    shippingCost: 14.00,
    brand: "FretHarmony",
    stockStatus: "OUT_OF_STOCK" as StockStatus,
    shippingTime: 6,
    active: true,
    images: [{
      imageId: "img_v3",
      url: "https://example.com/violin3.jpg",
      altText: "FretHarmony Arco Violin"
    }]
  },
  {
    id: 12,
    name: "StringSpirit Alto",
    category: "VIOLIN" as ProductCategory,
    description: "Rich-toned violin suitable for intermediate players.",
    price: 610.00,
    shippingCost: 7.00,
    brand: "StringSpirit",
    stockStatus: "LOW_STOCK" as StockStatus,
    shippingTime: 3,
    active: true,
    images: [{
      imageId: "img_v4",
      url: "https://example.com/violin4.jpg",
      altText: "StringSpirit Alto Violin"
    }]
  },
];