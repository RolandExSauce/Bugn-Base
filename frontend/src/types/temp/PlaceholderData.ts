import type { MessageDto, Order, Product, User } from "../models";

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

export const mockProduct: Product = {
  id: "1",
  name: "Placeholder product name",
  category: "guitar",
  description: `Lorem ipsum dolor sit, amet consectetur adipisicing elit. Enim
    dolor, doloribus fuga temporibus aspernatur odio rerum error labore
    adipisci magni pariatur ex facilis excepturi dicta consectetur r sit
    amet consectetur adipisicing elit. Sequi ipsa quis nam quas, veniam
    sapiente, nesciunt asperiores maxime odit, vitae adipisci quisquam
    cupiditate tenetur officiis error inventore? Dolorem, sit illum?`,
  price: 499.99,
  shippingCost: 15.0,
  brand: "New Brand",
  stockStatus: true,
  shippingTime: 3,
  active: true,
};

export const mockUser: User = {
  id: "1",
  firstname: "Alice",
  lastname: "Smith",
  phone: 12345,
  address: "Street 1",
  postcode: 1000,
  email: "alice@mail.com",
  active: true,
  createdAt: new Date(),
  role: "ADMIN",
};

export const mockUser2: User = {
  id: "2",
  firstname: "Emmy",
  lastname: "Smith",
  phone: 12345,
  address: "Street 1",
  postcode: 1000,
  email: "alice@mail.com",
  active: true,
  createdAt: new Date(),
  role: "USER",
};

export const mockUser3: User = {
  id: "3",
  firstname: "Otto",
  lastname: "Smith",
  phone: 12345,
  address: "Street 1",
  postcode: 1000,
  email: "alice@mail.com",
  active: true,
  createdAt: new Date(),
  role: "USER",
};

export const mockOrder: Order = {
  id: "ORD1001",
  user: mockUser,
  orderDate: new Date("2025-10-11T14:00:00"),
  totalAmount: mockProduct.price * 3 + mockProduct.shippingCost * 3, // example total
  deliveryFullname: "John Doe",
  deliveryAddress: "123 Main Street",
  deliveryPostcode: 10115,
  paymentMethod: "creditcard",
  deliveryStatus: "pending",
  items: [
    { product: mockProduct, quantity: 2 },
    { product: mockProduct, quantity: 1 },
  ],
};

export const mockProducts: Product[] = [
  // piano
  {
    id: "P1",
    name: "PrestoTone Mini",
    category: "piano",
    description:
      "Compact digital piano with weighted keys and simple controls.",
    price: 45000,
    shippingCost: 0,
    brand: "PrestoTune piano",
    stockStatus: true,
    shippingTime: 3,
    active: true,
  },
  {
    id: "P2",
    name: "StrumVista Compact",
    category: "piano",
    description: "Entry-level upright piano with balanced tone.",
    price: 78000,
    shippingCost: 1200,
    brand: "StrumVista Black",
    stockStatus: true,
    shippingTime: 5,
    active: true,
  },
  {
    id: "P3",
    name: "EchoLynx Studio 44",
    category: "piano",
    description: "Studio-focused piano with enhanced resonance system.",
    price: 110000,
    shippingCost: 1500,
    brand: "EchoLynx Crafters",
    stockStatus: false,
    shippingTime: 6,
    active: false,
  },
  {
    id: "P4",
    name: "SonataCraft Micro Grand",
    category: "piano",
    description: "Small-form grand piano with concert-style dynamics.",
    price: 310000,
    shippingCost: 3000,
    brand: "SonataCraft piano",
    stockStatus: true,
    shippingTime: 4,
    active: true,
  },

  // guitar
  {
    id: "G1",
    name: "ToneRiver Classic",
    category: "guitar",
    description: "Traditional acoustic guitar with warm midrange.",
    price: 59000,
    shippingCost: 900,
    brand: "ToneRiver",
    stockStatus: true,
    shippingTime: 5,
    active: true,
  },
  {
    id: "G2",
    name: "FretHarmony Edge",
    category: "guitar",
    description: "Electric guitar with modern pickup configuration.",
    price: 99000,
    shippingCost: 1500,
    brand: "FretHarmony Instruments",
    stockStatus: true,
    shippingTime: 2,
    active: true,
  },
  {
    id: "G3",
    name: "EchoRidge Touring",
    category: "guitar",
    description: "Tour-ready guitar with reinforced body and clean output.",
    price: 145000,
    shippingCost: 2100,
    brand: "EchoRidge Keyboards",
    stockStatus: false,
    shippingTime: 8,
    active: false,
  },
  {
    id: "G4",
    name: "StringSpirit Solo",
    category: "guitar",
    description: "Lightweight guitar designed for flexible solo performance.",
    price: 72000,
    shippingCost: 700,
    brand: "StringSpirit",
    stockStatus: true,
    shippingTime: 3,
    active: true,
  },

  // violin
  {
    id: "V1",
    name: "ClassicKeys Mini Violin",
    category: "violin",
    description: "Compact violin with clean projection and durable build.",
    price: 55000,
    shippingCost: 800,
    brand: "ClassicKeys Co.",
    stockStatus: true,
    shippingTime: 4,
    active: true,
  },
  {
    id: "V2",
    name: "Virtuoso Nova",
    category: "violin",
    description: "Hand-carved violin with warm upper-register clarity.",
    price: 88000,
    shippingCost: 1200,
    brand: "Virtuoso Makers",
    stockStatus: true,
    shippingTime: 2,
    active: true,
  },
  {
    id: "V3",
    name: "FretHarmony Arco",
    category: "violin",
    description: "Violin with balanced overtones and responsive bow handling.",
    price: 97000,
    shippingCost: 1400,
    brand: "FretHarmony Instruments",
    stockStatus: false,
    shippingTime: 6,
    active: true,
  },
  {
    id: "V4",
    name: "StringSpirit Alto",
    category: "violin",
    description: "Rich-toned violin suitable for intermediate players.",
    price: 61000,
    shippingCost: 700,
    brand: "StringSpirit",
    stockStatus: true,
    shippingTime: 3,
    active: true,
  },
];
