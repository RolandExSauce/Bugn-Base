export default function ProductFilter() {
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
              name="instrument"
              value="gitarre"
            />
            Gitarre
          </label>
          <label>
            <input
              className="filter-instrument-radio"
              type="radio"
              name="instrument"
              value="klavier"
            />
            Klavier
          </label>
          <label>
            <input
              className="filter-instrument-radio"
              type="radio"
              name="instrument"
              value="schlagzeug"
            />
            Schlagzeug
          </label>
        </fieldset>

        <fieldset>
          <legend>Preis</legend>
          <label htmlFor="price-sort"></label>
          <select id="price-sort" name="priceSort" defaultValue="">
            <option value="">Keine Sortierung</option>
            <option value="asc">Aufsteigend</option>
            <option value="desc">Absteigend</option>
          </select>
        </fieldset>

        <fieldset className="brand-fieldset">
          <legend>Marke</legend>
          <div className="filter-brands">
            {Array.from({ length: 10 }).map((_, i) => (
              <label key={i}>
                <input
                  className="filter-brands-checkbox"
                  type="checkbox"
                  name="brand"
                  value={`brand-${i}`}
                />
                Marke {i}
              </label>
            ))}
          </div>
        </fieldset>

        <fieldset>
          <legend>Bewertung</legend>
          <label htmlFor="rating"></label>
          <select name="rating" defaultValue="">
            <option value="">Alle Bewertungen</option>
            <option value="5">★★★★★ (5)</option>
            <option value="4">★★★★☆ (4+)</option>
            <option value="3">★★★☆☆ (3+)</option>
            <option value="2">★★☆☆☆ (2+)</option>
            <option value="1">★☆☆☆☆ (1+)</option>
          </select>
        </fieldset>

        <div className="filter-actions d-flex flex-column row-gap-2 align-items-center ">
          <button className="filter-apply-button" type="submit">
            <img src="/apply-filter.svg" alt="Apply filter icon" />
            Anwenden
          </button>

          <button className="filter-reset-button" type="button">
            <img src="/filter-reset.svg" alt="Reset filter icon" />
            Zurücksetzen
          </button>
        </div>
      </form>
    </aside>
  );
}
