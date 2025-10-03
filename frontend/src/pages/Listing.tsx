import ProductFilter from "../components/ProductFilter";
import ProductItem from "../components/ProductItem";

export default function Listing() {
  return (
    <div className="listing-main">
      <ProductFilter />
      <div className="listing-products">
        {/* render the products here here  */}
        {Array.from({ length: 10 }).map((_, i) => (
          <ProductItem key={i} />
        ))}
        <img src="/g-1.jpg" alt="" />
      </div>
    </div>
  );
}
