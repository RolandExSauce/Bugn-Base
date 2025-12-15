import { Link } from "react-router-dom";
import ShopItem from "../components/product/ShopItem";
import { useState, useEffect, useCallback } from "react";
import Searchbar from "../components/navigation/Searchbar";
import type { Product } from "../types/models";
import ShopService from "../services/shop/shop.service";

export default function HomePage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Fetch products on component mount
  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      const data = await ShopService.getProducts();
      console.log("got data: ", data)
      setProducts(data);
      setError(null);
    } catch (err) {
      setError("Produkte konnten nicht geladen werden.");
      console.error("Error fetching products:", err);
    } finally {
      setLoading(false);
    }
  };

  // Handle search from Searchbar
  const handleSearch = useCallback(async (searchTerm: string) => {
    if (!searchTerm.trim()) {
      fetchProducts(); // Reset to initial products
      return;
    }

    try {
      setLoading(true);
      const data = await ShopService.getProducts({
        name: searchTerm,
        pageSize: 12, // Show more results for search
      });
      setProducts(data);
      setError(null);
    } catch (err) {
      setError("Suche fehlgeschlagen.");
      console.error("Search error:", err);
    } finally {
      setLoading(false);
    }
  }, []);

  return (
    <div className="homepage-main d-flex flex-column align-items-center text-center gap-4 p-4">
      <h1>Willkommen!</h1>
      <h2>Entdecken Sie hochwertige Instrumente</h2>

      <Link to="/listing" className="btn btn-primary">
        Jetzt entdecken
      </Link>

      <Searchbar onSearch={handleSearch} />
     
      {loading ? (
        <div className="mt-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-2">Lade Produkte...</p>
        </div>
      ) : error ? (
        <div className="alert alert-danger mt-4" role="alert">
          {error}
          <button 
            onClick={fetchProducts}
            className="btn btn-sm btn-outline-danger ms-3"
          >
            Erneut versuchen
          </button>
        </div>
      ) : products.length === 0 ? (
        <div className="alert alert-info mt-4" role="alert">
          Keine Produkte gefunden.
        </div>
      ) : (
        <div className="d-flex flex-wrap justify-content-center gap-4 mt-4">
          {products.map((p) => (
            <ShopItem key={p.id} product={p} />
          ))}
        </div>
      )} 
    </div>
  );
}
