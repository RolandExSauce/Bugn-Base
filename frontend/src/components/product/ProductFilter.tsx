export default function ProductFilter() {
  return (
    <aside className="filter-sidebar" aria-label="Produktfilter">
      <span className="filter-title">
        {" "}
        <img src="/filter.svg" alt="" /> Filter
      </span>
      <form className="product-filter" aria-describedby="filter-desc">
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
          <label htmlFor="price-sort" className="sr-only"></label>
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
            {/* render fetched brands here, e.g.
            brands.length === 0 ? <p className="loading">Marken werden geladen…</p>
            : brands.map(b => (
                <label key={b}>
                  <input type="checkbox" name="brand" value={b} /> {b}
                </label>
              ))
        */}
          </div>
        </fieldset>

        <fieldset>
          <legend>Bewertung</legend>
          <label htmlFor="rating" className="sr-only"></label>
          <select id="rating" name="rating" defaultValue="">
            <option value="">Alle Bewertungen</option>
            <option value="5">★★★★★ (5)</option>
            <option value="4">★★★★☆ (4+)</option>
            <option value="3">★★★☆☆ (3+)</option>
            <option value="2">★★☆☆☆ (2+)</option>
            <option value="1">★☆☆☆☆ (1+)</option>
          </select>
        </fieldset>

        <div className="filter-actions">
          <button type="submit">Anwenden</button>
          <button type="button" id="reset">
            Zurücksetzen
          </button>
        </div>
      </form>
    </aside>
  );
}
