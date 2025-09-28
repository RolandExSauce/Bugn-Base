export default interface UserDto {
  userId: string;
  email: string;
  phone: string;
  password: string;
  firstName: string;
  lastName: string;
  address: string;
  postcode: number;
  role: string;
  active: boolean;
  createdAt: Date;
}
