import { Link } from "react-router-dom";
import ProductFilter from "../components/product/ProductFilter";
import ShopItem from "../components/product/ShopItem";
import { useEffect, useState } from "react";
import type { Product, FilterDto } from "../types/models";
import { mockProducts } from "../api/mock";


export default function Listing() {
  const [products, setProducts] = useState<Product[]>(mockProducts);

  useEffect(() => {
    // todo: fetch products initially
    // Example: ShopService.getProducts({ category: "GUITAR" })
  }, []);

  const applyFilter = (filter: FilterDto) => {
    // Filter locally for now, later connect to API
    let filtered = [...mockProducts];
    
    // Filter by category
    if (filter.category) {
      filtered = filtered.filter(p => p.category === filter.category);
    }
    
    // Filter by brands
    if (filter.brands && filter.brands.length > 0) {
      filtered = filtered.filter(p => filter.brands.includes(p.brand));
    }
    
    // Sort by price
    if (filter.sort === "price-asc") {
      filtered.sort((a, b) => a.price - b.price);
    } else if (filter.sort === "price-desc") {
      filtered.sort((a, b) => b.price - a.price);
    }
    
    // Filter by stars (rating) - you'll need to add rating to Product type
    // if (filter.stars) {
    //   filtered = filtered.filter(p => p.rating >= filter.stars);
    // }
    
    setProducts(filtered);
    
    // Later with API:
    // ShopService.getProducts({
    //   category: filter.category,
    //   brand: filter.brands,
    //   // Add other filters
    // }).then(data => setProducts(data));
  };

  return (
    <div className="listing-main d-flex mt-5 pt-4 w-100 column-gap-5">
      <div>
        <ProductFilter applyFilter={applyFilter} />
      </div>

      <div className="listing-products flex-grow-1 d-flex flex-wrap column-gap-4 row-gap-5">
        {products.map((p) => (
          <Link
            to={`/product/${p.id}`}
            key={p.id}
            className="shop-item-container flex-grow-1"
          >
            <ShopItem product={p} />
          </Link>
        ))}
      </div>
    </div>
  );
}