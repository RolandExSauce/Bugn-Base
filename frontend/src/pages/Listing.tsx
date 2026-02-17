import { Link } from "react-router-dom";
import ProductFilter from "../components/product/ProductFilter";
import ShopItem from "../components/product/ShopItem";
import { useCallback, useEffect, useMemo, useState } from "react";
import type { Product, ProductFilter as FilterType } from "../types/models";
import ShopService from "../services/shop.service";
import Searchbar from "../components/navigation/Searchbar";

export default function Listing() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentFilter, setCurrentFilter] = useState<FilterType | null>(null);
  const [searchTerm, setSearchTerm] = useState("");

  // ✅ Brands aus echten Produkten berechnen
  const availableBrands = useMemo(() => {
    const brands = Array.from(
      new Set(products.map((p) => p.brand).filter(Boolean))
    ) as string[];
    brands.sort((a, b) => a.localeCompare(b));
    return brands;
  }, [products]);

  useEffect(() => {
    void fetchProductsWithFilter(null);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const toApiFilter = (filter: FilterType | null) => {
  const brands =
    filter?.brand == null
      ? undefined
      : Array.isArray(filter.brand)
        ? filter.brand.filter(Boolean)
        : [filter.brand].filter(Boolean);

  return {
    name: filter?.name?.trim() || undefined,
    category: filter?.category || undefined,
    priceMin: filter?.priceMin ?? undefined,
    priceMax: filter?.priceMax ?? undefined,
    brand: brands && brands.length ? brands : undefined,

    stars: filter?.stars, // ✅ null weg

    pageNumber: filter?.pageNumber ?? 0,
    pageSize: filter?.pageSize ?? 50,
  };
};


  const fetchProductsWithFilter = async (filter: FilterType | null) => {
    try {
      setLoading(true);
      setError(null);
      setCurrentFilter(filter);

      const apiFilter = toApiFilter(filter);
      const data = await ShopService.getProducts(apiFilter);

      const sortedData = [...data];
      if (filter?.sort === "price-asc") {
        sortedData.sort((a, b) => a.price - b.price);
      } else if (filter?.sort === "price-desc") {
        sortedData.sort((a, b) => b.price - a.price);
      }
      // Hinweis: rating-sort geht nur, wenn es ein rating Feld gibt

      setProducts(sortedData);
    } catch (err) {
      setError("Produkte konnten nicht geladen werden.");
      console.error("Error fetching products:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleApplyFilter = (filter: FilterType) => {
    void fetchProductsWithFilter(filter);
  };

  const handleResetFilters = () => {
    setSearchTerm("");
    void fetchProductsWithFilter(null);
  };

  // ✅ Search kombiniert sich mit currentFilter (statt alles zu überschreiben)
  const handleSearch = useCallback(
    async (term: string) => {
      setSearchTerm(term);

      const combined: FilterType = {
        ...(currentFilter ?? {}),
        name: term,
        pageNumber: 0,
      };

      void fetchProductsWithFilter(combined);
    },
    [currentFilter]
  );

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
      <div className="mt-4 d-flex w-100 justify-content-center">
        <Searchbar searchTerm={searchTerm} onSearch={handleSearch} />
      </div>

      <div className="pt-4 pb-5 listing-main d-flex w-100 column-gap-5">
        <div>
          <ProductFilter
            applyFilter={handleApplyFilter}
            currentFilter={currentFilter}
            availableBrands={availableBrands} // ✅ FIX: Prop übergeben
          />
        </div>

        <div className="listing-products flex-grow-1 d-flex flex-wrap column-gap-4 row-gap-5">
          {products.map((p, i) => (
            <Link
              to={`/product/${p.id}`}
              key={p.id}
              className="shop-item-container flex-grow-1"
            >
              <ShopItem key={p.id + i} product={p} />
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
}
