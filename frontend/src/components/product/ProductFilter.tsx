import { useState } from "react";
import type { FilterDto, ItemCategory, SortType } from "../../types/models";

export default function ProductFilter({
  applyFilter,
}: {
  applyFilter: (filter: FilterDto) => void;
}) {
  const [filter, setFilter] = useState<FilterDto>({
    category: "guitar",
    brands: [],
    sort: "",
    stars: undefined,
  });

  const updateCategory = (category: ItemCategory) => {
    setFilter((prev) => ({ ...prev, category }));
  };

  const handleBrandChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newBrands = filter.brands;

    if (newBrands?.includes(e.target.value)) {
      newBrands?.splice(newBrands.indexOf(e.target.value), 1);
    } else {
      newBrands?.push(e.target.value);
    }

    setFilter((prev) => ({ ...prev, brands: newBrands }));
  };

  const handleResetFilter = () => {
    const newFilter: FilterDto = {
      category: "guitar",
      brands: [],
      sort: "",
      stars: undefined,
    };
    setFilter(newFilter);
    applyFilter(newFilter);
  };

  return (
    <aside className="filter-sidebar" aria-label="Produktfilter">
      <span className="filter-title">
        <img src="/filter.svg" alt="" /> Filter
      </span>

      <form className="product-filter d-flex flex-column row-gap-3 h-100 pe-3 ">
        <fieldset className="instrument-fieldset">
          <legend>Instrumententyp</legend>

          <label>
            <input
              className="filter-instrument-radio"
              type="radio"
              name="category"
              value="guitar"
              checked={filter.category === "guitar"}
              onChange={() => updateCategory("guitar")}
            />
            Gitarre
          </label>

          <label>
            <input
              className="filter-instrument-radio"
              type="radio"
              name="category"
              value="piano"
              checked={filter.category === "piano"}
              onChange={() => updateCategory("piano")}
            />
            Klavier
          </label>

          <label>
            <input
              className="filter-instrument-radio"
              type="radio"
              name="category"
              value="drum"
              checked={filter.category === "violin"}
              onChange={() => updateCategory("violin")}
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
            <option value="price-asc">Aufsteigend</option>
            <option value="price-desc">Absteigend</option>
          </select>
        </fieldset>

        <fieldset className="brand-fieldset">
          <legend>Marke</legend>
          <div className="filter-brands">
            {/* here render all brands in selected category */}
            {Array.from({ length: 10 }).map((_, i) => (
              <label key={i}>
                <input
                  className="filter-brands-checkbox"
                  type="checkbox"
                  name="brand"
                  value={`brand-${i}`}
                  onChange={handleBrandChange}
                  checked={filter.brands?.includes(`brand-${i}`)}
                />
                Marke {i}
              </label>
            ))}
          </div>
        </fieldset>

        <fieldset>
          <legend>Bewertung</legend>
          <select
            name="stars"
            value={filter.stars ?? ""}
            onChange={(e) =>
              setFilter((prev) => ({ ...prev, stars: Number(e.target.value) }))
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
          <button
            className="filter-apply-button"
            type="submit"
            onClick={() => applyFilter(filter)}
          >
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
