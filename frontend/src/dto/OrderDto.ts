import type ProductDto from "./ProductDto";

export default interface OrderDto {
  orderId: string;
  userId: string;
  orderDate: Date;
  totalAmount: number;
  deliveryFullname: string;
  deliveryAddress: string;
  deliveryPostcode: number;
  paymentMethod: string;
  orderStatus: string;
  deliveryStatus: string;
  items: Map<number, ProductDto>; // amount: Product
}
