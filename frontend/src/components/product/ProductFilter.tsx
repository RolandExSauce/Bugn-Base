import { useEffect, useState } from "react";
import type {
  ProductFilter,
  ProductCategory,
  SortType,
} from "../../types/models";
import { mockProducts } from "../../api/mock";

// Extract unique brands from mock products for each category
const getBrandsByCategory = (category: ProductCategory): string[] => {
  const brands = mockProducts
    .filter((p) => p.category === category)
    .map((p) => p.brand);
  return [...new Set(brands)]; // Remove duplicates
};

export default function ProductFilterComponent({
  applyFilter,
  currentFilter,
}: {
  applyFilter: (filter: ProductFilter) => void;
  currentFilter: ProductFilter | null;
}) {
  const [filter, setFilter] = useState<ProductFilter>({
    category: "GUITARS",
    brand: [],
    sort: "",
    stars: undefined,
  });

  const [availableBrands, setAvailableBrands] = useState<string[]>(
    getBrandsByCategory("GUITARS")
  );

  // Update local state when parent's currentFilter changes
  useEffect(() => {
    if (currentFilter) {
      setFilter(currentFilter);
      // Also update available brands based on category
      const newBrands = getBrandsByCategory(
        currentFilter.category || "GUITARS"
      );
      setAvailableBrands(newBrands);
    } else {
      // Reset to defaults when parent has no filter
      const defaultFilter: ProductFilter = {
        category: "GUITARS",
        brand: [],
        sort: "",
        stars: undefined,
      };
      setFilter(defaultFilter);
      setAvailableBrands(getBrandsByCategory("GUITARS"));
    }
  }, [currentFilter]);

  const updateCategory = (category: ProductCategory) => {
    const newBrands = getBrandsByCategory(category);
    setAvailableBrands(newBrands);

    // Reset brands when category changes
    setFilter((prev) => ({
      ...prev,
      category,
      brand: (prev.brand || []).filter((brand) => newBrands.includes(brand)),
    }));
  };

  const handleBrandChange = (brand: string) => {
    const currentBrands = filter.brand || [];
    const newBrands = currentBrands.includes(brand)
      ? currentBrands.filter((b) => b !== brand)
      : [...currentBrands, brand];

    setFilter((prev) => ({ ...prev, brand: newBrands }));
  };

  const handleResetFilter = () => {
    const newFilter: ProductFilter = {
      category: "GUITARS",
      brand: [],
      sort: "",
      stars: undefined,
    };
    setFilter(newFilter);
    setAvailableBrands(getBrandsByCategory("GUITARS"));
    applyFilter(newFilter);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    applyFilter(filter);
  };

  return (
    <aside className="filter-sidebar" aria-label="Produktfilter">
      <span className="filter-title">
        <img src="/filter.svg" alt="" /> Filter
      </span>

      <form
        className="product-filter d-flex flex-column row-gap-3 h-100 pe-3"
        onSubmit={handleSubmit}
      >
        <fieldset className="instrument-fieldset">
          <legend>Instrumententyp</legend>

          <label>
            <input
              className="filter-instrument-radio"
              type="radio"
              name="category"
              value="GUITAR"
              checked={filter.category === "GUITARS"}
              onChange={() => updateCategory("GUITARS")}
            />
            Gitarre
          </label>

          <label>
            <input
              className="filter-instrument-radio"
              type="radio"
              name="category"
              value="PIANO"
              checked={filter.category === "PIANOS"}
              onChange={() => updateCategory("PIANOS")}
            />
            Klavier
          </label>

          <label>
            <input
              className="filter-instrument-radio"
              type="radio"
              name="category"
              value="VIOLIN"
              checked={filter.category === "VIOLINS"}
              onChange={() => updateCategory("VIOLINS")}
            />
            Violine
          </label>
        </fieldset>

        <fieldset>
          <legend>Preis</legend>
          <select
            id="price-sort"
            name="sort"
            value={filter.sort ?? ""}
            onChange={(e) =>
              setFilter((prev) => ({
                ...prev,
                sort: e.target.value as SortType,
              }))
            }
          >
            <option value="">Keine Sortierung</option>
            <option value="price-asc">Aufsteigend (niedrig zu hoch)</option>
            <option value="price-desc">Absteigend (hoch zu niedrig)</option>
          </select>
        </fieldset>

        <fieldset className="brand-fieldset">
          <legend>Marke</legend>
          <div className="filter-brands">
            {availableBrands.map((brand, i) => (
              <label key={i}>
                <input
                  className="filter-brands-checkbox"
                  type="checkbox"
                  name="brand"
                  value={brand}
                  onChange={() => handleBrandChange(brand)}
                  checked={(filter.brand || []).includes(brand)}
                />
                {brand}
              </label>
            ))}
            {availableBrands.length === 0 && (
              <p className="small text-muted">
                Keine Marken in dieser Kategorie
              </p>
            )}
          </div>
        </fieldset>

        <fieldset>
          <legend>Bewertung</legend>
          <select
            name="stars"
            value={filter.stars ?? ""}
            onChange={(e) =>
              setFilter((prev) => ({
                ...prev,
                stars: e.target.value ? Number(e.target.value) : undefined,
              }))
            }
          >
            <option value="">Alle Bewertungen</option>
            <option value="5">★★★★★ (5)</option>
            <option value="4">★★★★☆ (4+)</option>
            <option value="3">★★★☆☆ (3+)</option>
            <option value="2">★★☆☆☆ (2+)</option>
            <option value="1">★☆☆☆☆ (1+)</option>
          </select>
        </fieldset>

        <div className="filter-actions d-flex flex-column row-gap-2 align-items-center">
          <button className="filter-apply-button" type="submit">
            <img src="/apply-filter.svg" alt="" />
            Anwenden
          </button>

          <button
            className="filter-reset-button"
            type="button"
            onClick={handleResetFilter}
          >
            <img src="/filter-reset.svg" alt="" />
            Zurücksetzen
          </button>
        </div>
      </form>
    </aside>
  );
}
