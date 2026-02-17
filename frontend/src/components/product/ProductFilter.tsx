import { useEffect, useState } from "react";
import type { ProductFilter, ProductCategory, SortType } from "../../types/models";

export default function ProductFilterComponent({
  applyFilter,
  currentFilter,
  availableBrands,
}: {
  applyFilter: (filter: ProductFilter) => void;
  currentFilter: ProductFilter | null;
  availableBrands: string[];
}) {
  const [filter, setFilter] = useState<ProductFilter>({
    category: undefined,   // ✅ kein default GUITARS
    brand: [],
    sort: "",
    stars: undefined,
  });

  useEffect(() => {
    if (currentFilter) {
      setFilter({
        ...currentFilter,
        brand: currentFilter.brand ?? [],
      });
    } else {
      setFilter({
        category: undefined,
        brand: [],
        sort: "",
        stars: undefined,
      });
    }
  }, [currentFilter]);

  const updateCategory = (category?: ProductCategory) => {
    // ✅ bei "Alle" category undefined setzen
    setFilter((prev) => ({
      ...prev,
      category,
      // Brands optional resetten, wenn du willst:
      // brand: [],
    }));
  };

  const handleBrandChange = (brand: string) => {
    setFilter((prev) => {
      const currentBrands = prev.brand ?? [];
      const newBrands = currentBrands.includes(brand)
        ? currentBrands.filter((b) => b !== brand)
        : [...currentBrands, brand];

      return { ...prev, brand: newBrands };
    });
  };

  const handleResetFilter = () => {
    const newFilter: ProductFilter = {
      category: undefined,
      brand: [],
      sort: "",
      stars: undefined,
    };
    setFilter(newFilter);
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

      <form className="product-filter d-flex flex-column row-gap-3 h-100 pe-3" onSubmit={handleSubmit}>
        <fieldset className="instrument-fieldset">
          <legend>Instrumententyp</legend>

          {/* ✅ Alle Kategorien */}
          <label>
            <input
              className="filter-instrument-radio"
              type="radio"
              name="category"
              checked={!filter.category}
              onChange={() => updateCategory(undefined)}
            />
            Alle
          </label>

          <label>
            <input
              className="filter-instrument-radio"
              type="radio"
              name="category"
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
            {availableBrands.map((brand) => (
              <label key={brand}>
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
              <p className="small text-muted">Keine Marken verfügbar</p>
            )}
          </div>
        </fieldset>

        {/* ⚠️ Bewertung: aktuell ohne Backend-Wirkung */}
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
          <p className="small text-muted mb-0">
            Hinweis: Bewertung funktioniert erst, wenn das Backend Ratings liefert.
          </p>
        </fieldset>

        <div className="filter-actions d-flex flex-column row-gap-2 align-items-center">
          <button className="filter-apply-button" type="submit">
            
            Anwenden
          </button>

          <button className="filter-reset-button" type="button" onClick={handleResetFilter}>
            
            Zurücksetzen
          </button>
        </div>
      </form>
    </aside>
  );
}
