import { Link } from "react-router-dom";
import ProductFilter from "../components/product/ProductFilter";
import ShopItem from "../components/product/ShopItem";
import { useCallback, useEffect, useState } from "react";
import type { Product, ProductFilter as FilterType } from "../types/models"; // Renamed import
import ShopService from "../services/shop.service";
import Searchbar from "../components/navigation/Searchbar";

export default function Listing() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentFilter, setCurrentFilter] = useState<FilterType | null>(null);
  const [searchTerm, setSearchTerm] = useState(""); // lifted search term

  useEffect(() => {
    fetchProductsWithFilter(null); // Fetch all products initially
  }, []);

  const fetchProductsWithFilter = async (filter: FilterType | null) => {
    try {
      setLoading(true);
      setCurrentFilter(filter);

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
      const sortedData = [...data];
      if (filter?.sort === "price-asc") {
        sortedData.sort((a, b) => a.price - b.price);
      } else if (filter?.sort === "price-desc") {
        sortedData.sort((a, b) => b.price - a.price);
      }

      setProducts(sortedData);
      setError(null);
    } catch (err) {
      setError("Produkte konnten nicht geladen werden.");
      console.error("Error fetching products:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleApplyFilter = (filter: FilterType) => {
    fetchProductsWithFilter(filter);
  };

  const handleResetFilters = () => {
    fetchProductsWithFilter(null);
  };

  const fetchProducts = async () => {
    try {
      setLoading(true);
      const data = await ShopService.getProducts();
      setProducts(data);
      setError(null);
    } catch (err) {
      setError("Produkte konnten nicht geladen werden.");
      console.error("Error fetching products:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = useCallback(async (term: string) => {
    setSearchTerm(term);

    setLoading(true);
    try {
      const data = await ShopService.getProducts({ name: term, pageSize: 12 });
      setProducts(data);
      setError(null);
    } catch (err) {
      setError("Suche fehlgeschlagen.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, []);

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
    <div>
      <div className="mt-4  d-flex w-100 justify-content-center">
        <Searchbar searchTerm={searchTerm} onSearch={handleSearch} />
      </div>
      <div className=" pt-4 listing-main d-flex w-100 column-gap-5">
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
    </div>
  );
}
