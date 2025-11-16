import { Link } from "react-router-dom";
import ProductFilter from "../components/product/ProductFilter";
import ShopItem from "../components/product/ShopItem";
import { useEffect, useState } from "react";
import type { Product } from "../types/models";
import { mockProducts } from "../types/temp/PlaceholderData";

export default function Listing() {
  const [products, setProducts] = useState<Product[]>(mockProducts);

  useEffect(() => {
    // todo: fetch products initially
  }, []);

  const applyFilter = (filter: FilterDto) => {
    // todo: apply filter
    // api call -> update products
  };

  return (
    <div className="listing-main d-flex mt-5 pt-4 w-100 column-gap-5">
      <div>
        <ProductFilter applyFilter={applyFilter} />
      </div>

      <div className="listing-products flex-grow-1 d-flex flex-wrap column-gap-4 row-gap-5">
        {products.map((p, i) => (
          <Link
            to={`/product/${p.id}`}
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
