import { Link } from "react-router-dom";
import ProductFilter from "../components/product/ProductFilter";
import ShopItem from "../components/product/ShopItem";

export default function Listing() {
  return (
    <div className="listing-main d-flex mt-5 pt-4 w-100 column-gap-5">
      <div>
        <ProductFilter />
      </div>

      {/* Products container grows */}
      <div className="listing-products flex-grow-1 d-flex flex-wrap column-gap-4 row-gap-5">
        {Array.from({ length: 10 }).map((_, i) => (
          <Link
            to={`/product/${i}`}
            key={i}
            className="shop-item-container flex-grow-1"
          >
            <ShopItem />
          </Link>
        ))}
      </div>
    </div>
  );
}
