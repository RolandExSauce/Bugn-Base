import type { Product, User } from "../models";

export const mockProduct: Product = {
  id: "1",
  name: "Product name",
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
  phone: "12345",
  address: "Street 1",
  postcode: 1000,
  email: "alice@mail.com",
  active: true,
  createdAt: new Date(),
  role: "USER",
};
