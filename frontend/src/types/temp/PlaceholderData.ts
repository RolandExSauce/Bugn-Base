import type { Order, Product, User } from "../models";

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
