import CartItem from "../cart/CartItem";

const ProductsList = () => {
  return (
    <div className="d-flex flex-column gap-3">
      {Array.from({ length: 3 }).map((_, index) => (
        <CartItem editable={true} key={index} />
      ))}
    </div>
  );
};
export default ProductsList;
