import { Link } from "react-router-dom";
import ProductFilter from "../components/product/ProductFilter";
import ShopItem from "../components/product/ShopItem";
import { useEffect, useState } from "react";
import type { Product, ProductFilter as FilterType } from "../types/models"; // Renamed import
import ShopService from "../services/shop.service";

export default function Listing() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentFilter, setCurrentFilter] = useState<FilterType | null>(null);

  // Fetch initial products on component mount
  useEffect(() => {
    fetchProductsWithFilter(null); // Fetch all products initially
  }, []);

  const fetchProductsWithFilter = async (filter: FilterType | null) => {
    try {
      setLoading(true);
      setCurrentFilter(filter);

      // KI SAYS: Convert frontend filter to backend API format
      //sort and stars are handled client-side
      // should adjust api 🤔
      const apiFilter = {
        name: filter?.name,
        category: filter?.category,
        priceMin: filter?.priceMin,
        priceMax: filter?.priceMax,
        brand: filter?.brand,
        pageNumber: filter?.pageNumber ?? 0,
        pageSize: filter?.pageSize ?? 50,
      };

      const data = await ShopService.getProducts(apiFilter);
      // console.log("filtered products: ", data);

      // Apply client-side sorting if backend doesn't support it
      const sortedData = [...data];
      if (filter?.sort === "price-asc") {
        sortedData.sort((a, b) => a.price - b.price);
      } else if (filter?.sort === "price-desc") {
        sortedData.sort((a, b) => b.price - a.price);
      }

      // TODO: For star ratings, fetch reviews and filter
      // This would require additional API endpoints

      setProducts(sortedData);
      setError(null);
    } catch (err) {
      setError("Produkte konnten nicht geladen werden.");
      console.error("Error fetching products:", err);
    } finally {
      setLoading(false);
    }
  };

  // This function is passed to ProductFilter and called when user clicks "Anwenden"
  const handleApplyFilter = (filter: FilterType) => {
    console.log("Applying filter:", filter);
    fetchProductsWithFilter(filter);
  };

  const handleResetFilters = () => {
    fetchProductsWithFilter(null);
  };

  if (loading) {
    return (
      <div className="listing-main d-flex justify-content-center align-items-center mt-5 pt-4 w-100">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 ms-3">Lade Produkte...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="listing-main d-flex justify-content-center align-items-center mt-5 pt-4 w-100">
        <div className="alert alert-danger">
          {error}
          <button
            onClick={handleResetFilters}
            className="btn btn-sm btn-outline-danger ms-3"
          >
            Erneut versuchen
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="listing-main d-flex mt-5 pt-4 w-100 column-gap-5">
      <div>
        <ProductFilter
          applyFilter={handleApplyFilter}
          currentFilter={currentFilter}
        />
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
