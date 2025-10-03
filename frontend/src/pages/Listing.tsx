import { Link } from "react-router-dom";
import ProductFilter from "../components/product/ProductFilter";
import ProductItem from "../components/product/ProductItem";

export default function Listing() {
  return (
    <div className="listing-main">
      <ProductFilter />
      <div className="listing-products">
        {/* render the products here here  */}
        {Array.from({ length: 10 }).map((_, i) => (
          <Link to={`/product/${i}`} key={i}>
            <ProductItem key={i} />
          </Link>
        ))}
      </div>
    </div>
  );
}
